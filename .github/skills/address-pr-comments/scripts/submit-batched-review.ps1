#!/usr/bin/env pwsh
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

param(
  [string]$Pr,
  [Parameter(Mandatory = $true)]
  [string]$RepliesFile,
  [Parameter(Mandatory = $true)]
  [string]$SummaryFile,
  [switch]$DryRun
)

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

if (-not (Test-Path -LiteralPath $RepliesFile)) {
  Fail-Json "Replies file not found: $RepliesFile"
}

if (-not (Test-Path -LiteralPath $SummaryFile)) {
  Fail-Json "Summary file not found: $SummaryFile"
}

$env:GH_PAGER = 'cat'

try {
  gh auth status *> $null
} catch {
  Fail-Json "GitHub CLI is not authenticated. Run 'gh auth login'."
}

$prNumber = Resolve-PrNumber $Pr
$replies = Get-Content -Raw -LiteralPath $RepliesFile | ConvertFrom-Json

if ($replies -isnot [System.Array]) {
  Fail-Json 'Replies file must be a JSON array of objects with threadId and body fields.'
}

foreach ($reply in $replies) {
  if (-not ($reply.PSObject.Properties.Name -contains 'threadId') -or -not ($reply.PSObject.Properties.Name -contains 'body')) {
    Fail-Json 'Replies file must be a JSON array of objects with threadId and body fields.'
  }
}

if ($DryRun) {
  [pscustomobject]@{
    ok = $true
    dryRun = $true
    pr = [int]$prNumber
    replyCount = @($replies).Count
  } | ConvertTo-Json -Depth 8
  exit 0
}

$prId = (gh pr view $prNumber --json id --jq '.id').Trim()
$summary = Get-Content -Raw -LiteralPath $SummaryFile

$createMutation = @'
mutation($pullRequestId: ID!) {
  addPullRequestReview(input: { pullRequestId: $pullRequestId }) {
    pullRequestReview { id }
  }
}
'@

$reviewCreate = gh api graphql -f "query=$createMutation" -f "pullRequestId=$prId" | ConvertFrom-Json
$reviewId = $reviewCreate.data.addPullRequestReview.pullRequestReview.id

$replyMutation = @'
mutation($reviewId: ID!, $threadId: ID!, $body: String!) {
  addPullRequestReviewThreadReply(
    input: {
      pullRequestReviewId: $reviewId
      pullRequestReviewThreadId: $threadId
      body: $body
    }
  ) {
    comment { id }
  }
}
'@

foreach ($reply in $replies) {
  gh api graphql -f "query=$replyMutation" -f "reviewId=$reviewId" -f "threadId=$($reply.threadId)" -f "body=$($reply.body)" *> $null
}

$submitMutation = @'
mutation($reviewId: ID!, $body: String!) {
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
}
'@

$submitted = gh api graphql -f "query=$submitMutation" -f "reviewId=$reviewId" -f "body=$summary" | ConvertFrom-Json

[pscustomobject]@{
  ok = $true
  replyCount = @($replies).Count
  reviewId = $submitted.data.submitPullRequestReview.pullRequestReview.id
  reviewUrl = $submitted.data.submitPullRequestReview.pullRequestReview.url
} | ConvertTo-Json -Depth 8
