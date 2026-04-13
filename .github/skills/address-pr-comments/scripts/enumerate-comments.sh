#!/bin/bash
set -euo pipefail

TMP_DIR="$(mktemp -d)"
cleanup() {
  rm -rf "$TMP_DIR"
}
trap cleanup EXIT

log() {
  echo "$*" >&2
}

fail() {
  local message="$1"
  jq -n --arg error "$message" '{ok:false,error:$error}'
  exit 1
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || fail "Missing required command: $1"
}

resolve_pr_number() {
  local input="${1:-}"

  if [[ -z "$input" ]]; then
    gh pr view --json number --jq '.number'
    return
  fi

  if [[ "$input" =~ ^[0-9]+$ ]]; then
    echo "$input"
    return
  fi

  if [[ "$input" =~ ^https://github\.com/[^/]+/[^/]+/pull/([0-9]+)(/.*)?$ ]]; then
    echo "${BASH_REMATCH[1]}"
    return
  fi

  fail "Expected a PR number, a GitHub PR URL, or no argument."
}

require_cmd gh
require_cmd git
require_cmd jq

export GH_PAGER=cat

gh auth status >/dev/null 2>&1 || fail "GitHub CLI is not authenticated. Run 'gh auth login'."
git rev-parse --show-toplevel >/dev/null 2>&1 || fail "Current directory is not a git repository."

pr_number="$(resolve_pr_number "${1:-}")"
current_branch="$(git branch --show-current)"

[[ -n "$current_branch" ]] || fail "Could not determine the current git branch."

pr_meta_json="$TMP_DIR/pr-meta.json"
gh pr view "$pr_number" --json id,number,title,url,headRefName > "$pr_meta_json"

pr_head_branch="$(jq -r '.headRefName' "$pr_meta_json")"
[[ "$current_branch" == "$pr_head_branch" ]] || fail "Current branch '$current_branch' does not match PR head branch '$pr_head_branch'."

if [[ -n "$(git diff --name-only --diff-filter=U)" ]]; then
  fail "Repository has unresolved merge conflicts."
fi

repo_json="$TMP_DIR/repo.json"
gh repo view --json owner,name > "$repo_json"

owner="$(jq -r '.owner.login' "$repo_json")"
repo="$(jq -r '.name' "$repo_json")"

threads_json="$TMP_DIR/threads.json"
printf '[]' > "$threads_json"

query='query($owner: String!, $repo: String!, $pr: Int!, $cursor: String) {
  repository(owner: $owner, name: $repo) {
    pullRequest(number: $pr) {
      reviewThreads(first: 100, after: $cursor) {
        nodes {
          id
          isResolved
          isOutdated
          path
          line
          startLine
          comments(first: 20) {
            nodes {
              databaseId
              body
              createdAt
              url
              author { login }
            }
          }
        }
        pageInfo {
          hasNextPage
          endCursor
        }
      }
    }
  }
}'

cursor=''
while :; do
  page_json="$TMP_DIR/page.json"

  if [[ -n "$cursor" ]]; then
    gh api graphql -f query="$query" -f owner="$owner" -f repo="$repo" -F pr="$pr_number" -f cursor="$cursor" > "$page_json"
  else
    gh api graphql -f query="$query" -f owner="$owner" -f repo="$repo" -F pr="$pr_number" > "$page_json"
  fi

  jq -s '.[0] + .[1]' "$threads_json" <(jq '.data.repository.pullRequest.reviewThreads.nodes' "$page_json") > "$TMP_DIR/threads-next.json"
  mv "$TMP_DIR/threads-next.json" "$threads_json"

  has_next_page="$(jq -r '.data.repository.pullRequest.reviewThreads.pageInfo.hasNextPage' "$page_json")"
  if [[ "$has_next_page" != "true" ]]; then
    break
  fi

  cursor="$(jq -r '.data.repository.pullRequest.reviewThreads.pageInfo.endCursor' "$page_json")"
done

jq \
  --slurpfile pr "$pr_meta_json" \
  --slurpfile repo "$repo_json" \
  --arg currentBranch "$current_branch" \
  '
  def preview(text):
    (text // "")
    | gsub("\\r?\\n"; " ")
    | if length > 280 then .[0:277] + "..." else . end;

  ($pr[0]) as $prm |
  ($repo[0]) as $repoInfo |
  {
    ok: true,
    pr: {
      number: $prm.number,
      id: $prm.id,
      title: $prm.title,
      url: $prm.url,
      owner: $repoInfo.owner.login,
      repo: $repoInfo.name,
      headRefName: $prm.headRefName,
      currentBranch: $currentBranch
    },
    summary: {
      totalThreads: length,
      activeThreads: map(select(.isResolved == false and .isOutdated == false)) | length
    },
    threads: map(select(.isResolved == false and .isOutdated == false))
      | map({
          threadId: .id,
          path: .path,
          line: .line,
          startLine: .startLine,
          commentCount: (.comments.nodes | length),
          reviewer: (.comments.nodes[0].author.login // "unknown"),
          latestComment: preview(.comments.nodes[-1].body),
          comments: (.comments.nodes | map({
            databaseId,
            author: (.author.login // "unknown"),
            createdAt,
            url,
            body
          }))
        })
  }
  ' "$threads_json"
