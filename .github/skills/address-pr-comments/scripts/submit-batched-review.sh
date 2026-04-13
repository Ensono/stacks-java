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
require_cmd jq

export GH_PAGER=cat

gh auth status >/dev/null 2>&1 || fail "GitHub CLI is not authenticated. Run 'gh auth login'."

pr_input=''
replies_file=''
summary_file=''
dry_run='false'

while [[ $# -gt 0 ]]; do
  case "$1" in
    --pr)
      pr_input="${2:-}"
      shift 2
      ;;
    --replies-file)
      replies_file="${2:-}"
      shift 2
      ;;
    --summary-file)
      summary_file="${2:-}"
      shift 2
      ;;
    --dry-run)
      dry_run='true'
      shift
      ;;
    *)
      fail "Unknown argument: $1"
      ;;
  esac
done

[[ -n "$replies_file" ]] || fail "Missing required argument: --replies-file"
[[ -n "$summary_file" ]] || fail "Missing required argument: --summary-file"
[[ -f "$replies_file" ]] || fail "Replies file not found: $replies_file"
[[ -f "$summary_file" ]] || fail "Summary file not found: $summary_file"

pr_number="$(resolve_pr_number "$pr_input")"
pr_id="$(gh pr view "$pr_number" --json id --jq '.id')"
review_summary="$(cat "$summary_file")"
reply_count="$(jq 'length' "$replies_file")"

jq -e 'type == "array" and all(.[]; has("threadId") and has("body"))' "$replies_file" >/dev/null || fail "Replies file must be a JSON array of objects with threadId and body fields."

if [[ "$dry_run" == 'true' ]]; then
  jq -n --argjson replyCount "$reply_count" --arg pr "$pr_number" '{ok:true,dryRun:true,pr:($pr|tonumber),replyCount:$replyCount}'
  exit 0
fi

create_mutation='mutation($pullRequestId: ID!) {
  addPullRequestReview(input: { pullRequestId: $pullRequestId }) {
    pullRequestReview { id }
  }
}'

review_id="$(gh api graphql -f query="$create_mutation" -f pullRequestId="$pr_id" --jq '.data.addPullRequestReview.pullRequestReview.id')"

reply_mutation='mutation($reviewId: ID!, $threadId: ID!, $body: String!) {
  addPullRequestReviewThreadReply(
    input: {
      pullRequestReviewId: $reviewId
      pullRequestReviewThreadId: $threadId
      body: $body
    }
  ) {
    comment { id }
  }
}'

while IFS= read -r reply; do
  thread_id="$(jq -r '.threadId' <<< "$reply")"
  body="$(jq -r '.body' <<< "$reply")"
  gh api graphql -f query="$reply_mutation" -f reviewId="$review_id" -f threadId="$thread_id" -f body="$body" >/dev/null
done < <(jq -c '.[]' "$replies_file")

submit_mutation='mutation($reviewId: ID!, $body: String!) {
  submitPullRequestReview(
    input: {
      pullRequestReviewId: $reviewId
      event: COMMENT
      body: $body
    }
  ) {
    pullRequestReview {
      id
      url
    }
  }
}'

gh api graphql -f query="$submit_mutation" -f reviewId="$review_id" -f body="$review_summary" > "$TMP_DIR/submitted.json"

jq --argjson replyCount "$reply_count" '{ok:true,replyCount:$replyCount,reviewId:.data.submitPullRequestReview.pullRequestReview.id,reviewUrl:.data.submitPullRequestReview.pullRequestReview.url}' "$TMP_DIR/submitted.json"
