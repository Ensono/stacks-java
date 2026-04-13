# Address PR Comments Workflow

This reference expands the skill without overloading `SKILL.md`.

## Enumeration Contract

The enumeration scripts output JSON with this shape:

```json
{
  "ok": true,
  "pr": {
    "number": 26,
    "id": "PR_kwDOExample",
    "title": "Fix edge case in parser",
    "url": "https://github.com/example/repo/pull/26",
    "owner": "example",
    "repo": "repo",
    "headRefName": "feature/fix-parser",
    "currentBranch": "feature/fix-parser"
  },
  "summary": {
    "totalThreads": 7,
    "activeThreads": 3
  },
  "threads": [
    {
      "threadId": "PRRT_kwDOExample",
      "path": "src/parser.ts",
      "line": 42,
      "startLine": 40,
      "commentCount": 2,
      "reviewer": "reviewer-handle",
      "latestComment": "Please handle empty input here.",
      "comments": [
        {
          "databaseId": 123456789,
          "author": "reviewer-handle",
          "createdAt": "2026-03-26T10:00:00Z",
          "url": "https://github.com/example/repo/pull/26#discussion_r123456789",
          "body": "Please handle empty input here."
        }
      ]
    }
  ]
}
```

If a hard precondition fails, the scripts return:

```json
{
  "ok": false,
  "error": "Reason"
}
```

## Classification Guidance

- Code change: reviewer requests a behavior or implementation update
- Documentation: reviewer requests comments, docs, examples, or README updates
- Test addition: reviewer requests coverage, regression tests, or missing assertions
- Clarification: reviewer asks why an existing approach is correct
- Deferred: valid item, but intentionally left for a follow-up issue or later PR
- Disagree with rationale: comment is not accepted, but requires a respectful explanation

## Local Validation Order

Run validation only after all requested changes are in place.

1. Build
2. Test
3. Lint
4. Pre-commit hooks if configured

Suggested discovery sequence:

1. Read repository docs and task definitions
2. Check package-manager scripts
3. Check language-native build tools
4. Check for `.pre-commit-config.yaml`, Husky hooks, or equivalent repository hooks

If the repository contains multiple modules, prioritize the touched modules first, then expand to whole-repo checks when the module-level checks are insufficient.

## Review Submission Contract

The batched review submission scripts expect:

- `replies.json`: array of `{ "threadId": string, "body": string }`
- `summary.md`: markdown or plain text review summary

Submission flow:

1. Create a pending review on the PR
2. Attach one reply per active thread
3. Submit the review with `COMMENT`

The submission scripts output JSON containing the created review id, URL, and reply count.

## Recommended Review Reply Style

Keep thread replies specific and short:

```text
Addressed in commit abc1234.
- Added the missing null guard
- Covered the empty-input case in tests
```

If no code change was made:

```text
No code change made here.
- The existing behavior is intentional because ...
- Added clarification in the review summary for future readers
```

## Failure Conditions

Stop and report instead of improvising when:

- The branch does not match the PR head branch
- Merge conflicts are present
- The review threads cannot be enumerated with `gh`
- Local validation fails and the failure is unrelated or ambiguous
- GitHub review batching fails and a fallback would create unwanted reviewer noise without explicit approval
