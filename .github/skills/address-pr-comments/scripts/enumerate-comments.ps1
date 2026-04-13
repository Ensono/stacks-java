#!/usr/bin/env pwsh
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Write-Status {
  param([string]$Message)
  [Console]::Error.WriteLine($Message)
}

function Fail-Json {
  param([string]$Message)
  [pscustomobject]@{
    ok = $false
    error = $Message
  } | ConvertTo-Json -Depth 8
  exit 1
}

function Resolve-PrNumber {
  param([string]$InputValue)

  if ([string]::IsNullOrWhiteSpace($InputValue)) {
    return (gh pr view --json number --jq '.number').Trim()
  }

  if ($InputValue -match '^[0-9]+$') {
    return $InputValue
  }

  if ($InputValue -match '^https://github\.com/[^/]+/[^/]+/pull/([0-9]+)(/.*)?$') {
    return $Matches[1]
  }

  Fail-Json 'Expected a PR number, a GitHub PR URL, or no argument.'
}

if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
  Fail-Json 'Missing required command: gh'
}

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
  Fail-Json 'Missing required command: git'
}

$env:GH_PAGER = 'cat'

try {
  gh auth status *> $null
} catch {
  Fail-Json "GitHub CLI is not authenticated. Run 'gh auth login'."
}

try {
  git rev-parse --show-toplevel *> $null
} catch {
  Fail-Json 'Current directory is not a git repository.'
}

$prNumber = Resolve-PrNumber $args[0]
$currentBranch = (git branch --show-current).Trim()

if ([string]::IsNullOrWhiteSpace($currentBranch)) {
  Fail-Json 'Could not determine the current git branch.'
}

$prMeta = gh pr view $prNumber --json id,number,title,url,headRefName | ConvertFrom-Json

if ($currentBranch -ne $prMeta.headRefName) {
  Fail-Json "Current branch '$currentBranch' does not match PR head branch '$($prMeta.headRefName)'."
}

$mergeConflicts = git diff --name-only --diff-filter=U
if (-not [string]::IsNullOrWhiteSpace(($mergeConflicts | Out-String).Trim())) {
  Fail-Json 'Repository has unresolved merge conflicts.'
}

$repoMeta = gh repo view --json owner,name | ConvertFrom-Json

$query = @'
query($owner: String!, $repo: String!, $pr: Int!, $cursor: String) {
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
}
'@

$allThreads = @()
$cursor = $null

while ($true) {
  $ghArgs = @('api', 'graphql', '-f', "query=$query", '-f', "owner=$($repoMeta.owner.login)", '-f', "repo=$($repoMeta.name)", '-F', "pr=$prNumber")
  if ($null -ne $cursor -and $cursor -ne '') {
    $ghArgs += @('-f', "cursor=$cursor")
  }

  $page = gh @ghArgs | ConvertFrom-Json
  $threads = @($page.data.repository.pullRequest.reviewThreads.nodes)
  if ($threads.Count -gt 0) {
    $allThreads += $threads
  }

  if (-not $page.data.repository.pullRequest.reviewThreads.pageInfo.hasNextPage) {
    break
  }

  $cursor = $page.data.repository.pullRequest.reviewThreads.pageInfo.endCursor
}

$activeThreads = foreach ($thread in $allThreads) {
  if (-not $thread.isResolved -and -not $thread.isOutdated) {
    $commentNodes = @($thread.comments.nodes)
    $latest = if ($commentNodes.Count -gt 0) { $commentNodes[-1].body } else { '' }
    $reviewer = if ($commentNodes.Count -gt 0 -and $commentNodes[0].author) { $commentNodes[0].author.login } else { 'unknown' }

    [pscustomobject]@{
      threadId = $thread.id
      path = $thread.path
      line = $thread.line
      startLine = $thread.startLine
      commentCount = $commentNodes.Count
      reviewer = $reviewer
      latestComment = (($latest -replace "`r?`n", ' ').Trim())
      comments = @(
        foreach ($comment in $commentNodes) {
          [pscustomobject]@{
            databaseId = $comment.databaseId
            author = if ($comment.author) { $comment.author.login } else { 'unknown' }
            createdAt = $comment.createdAt
            url = $comment.url
            body = $comment.body
          }
        }
      )
    }
  }
}

[pscustomobject]@{
  ok = $true
  pr = [pscustomobject]@{
    number = $prMeta.number
    id = $prMeta.id
    title = $prMeta.title
    url = $prMeta.url
    owner = $repoMeta.owner.login
    repo = $repoMeta.name
    headRefName = $prMeta.headRefName
    currentBranch = $currentBranch
  }
  summary = [pscustomobject]@{
    totalThreads = @($allThreads).Count
    activeThreads = @($activeThreads).Count
  }
  threads = @($activeThreads)
} | ConvertTo-Json -Depth 10
