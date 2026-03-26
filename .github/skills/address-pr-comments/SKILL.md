---
name: address-pr-comments
description: Address all active GitHub PR review threads with one batched review. Use when asked to resolve PR comments, address review feedback, or reply to unresolved pull request comments.
metadata:
  author: GitHub Copilot
  version: "1.0.0"
  argument-hint: "[pr-number-or-url]"
---

# Address PR Comments

Resolve active GitHub pull request review threads end-to-end: enumerate unresolved non-outdated threads, make the requested code or documentation changes, run the local quality gates after all requested changes are in place, then submit one batched review with a reply for every addressed thread.

## How It Works

1. Enumerate all active review threads with the helper script. Only work from threads where `isResolved` is `false` and `isOutdated` is `false`.
2. Review each thread, inspect the referenced file context, and make the smallest complete change that resolves the feedback. If a comment should not result in a code change, prepare a clear rationale for the review reply instead.
3. After all requested changes are complete, run the repository's local quality gates in this order: build, test, lint. If the repository exposes pre-commit hooks, run those before finalizing.
4. Commit and push the changes, prepare one reply per active thread, and submit a single batched review so reviewers receive one grouped notification.

## Scripts

Use Bash when a POSIX shell is available:

```bash
bash /mnt/skills/user/address-pr-comments/scripts/enumerate-comments.sh [pr-number-or-url]
bash /mnt/skills/user/address-pr-comments/scripts/submit-batched-review.sh --pr <pr-number-or-url> --replies-file replies.json --summary-file summary.md
```

Use PowerShell when the environment is Windows-native or PowerShell-first:

```powershell
pwsh /mnt/skills/user/address-pr-comments/scripts/enumerate-comments.ps1 [pr-number-or-url]
pwsh /mnt/skills/user/address-pr-comments/scripts/submit-batched-review.ps1 -Pr <pr-number-or-url> -RepliesFile replies.json -SummaryFile summary.md
```

## Inputs

- Optional PR identifier: a PR number such as `26`, a PR URL, or no argument to use the PR attached to the current branch.
- `replies.json`: an array of objects with `threadId` and `body` fields.
- `summary.md`: the overall review summary that will be submitted with the batched review.

## Required Workflow

1. Run the enumeration script and build the working list from its JSON output. Do not rely on `gh pr view --comments`; use review threads so resolution state is preserved.
2. For every active thread:
   - Read the relevant file context.
   - Classify the thread as code change, documentation, tests, clarification, deferred, or disagree-with-rationale.
   - Implement the fix or prepare the explanation.
   - Track the exact files and validation impact.
3. When every active thread has been addressed locally, run local validation:
   - Build
   - Test
   - Lint
   - Pre-commit hooks if `.pre-commit-config.yaml` or equivalent hook tooling is present
4. If validation fails, fix the failures before creating the review.
5. Stage, commit, and push the changes.
6. Prepare `replies.json` so each active thread receives one specific response describing what changed or why no code change was needed.
7. Submit exactly one pending review with all thread replies attached, then submit that review once.

## Operating Rules

- Stop if the current branch does not match the PR head branch.
- Stop if the repository has unresolved merge conflicts.
- Preserve existing signing, branch protection, and repository workflow requirements.
- Prefer minimal, root-cause changes over broad refactors.
- If a reviewer request is invalid or out of scope, do not force a code change. Reply with a concise rationale and note any required follow-up.
- If batching is unavailable, state that limitation before falling back to individual replies.

## Quality Gate Detection

Use the repository's native commands instead of guessing generic ones. Prefer, in order:

1. Repository documentation and existing task runners
2. Package manager scripts such as `npm`, `pnpm`, `yarn`, or `bun`
3. Language-native commands such as `mvn`, `gradle`, `dotnet`, `pytest`, `go test`, or `cargo`
4. Pre-commit via `pre-commit run --all-files` when configured

If the repository exposes multiple build or test entry points, run the narrowest set that fully covers the changed surface area, then widen scope if failures indicate broader impact.

## Reply File Schema

```json
[
  {
    "threadId": "PRRT_kwDOExample",
    "body": "Addressed in commit abc1234.\n- Added the missing null handling\n- Extended tests for the empty-input path"
  }
]
```

## Expected Outputs

- Enumeration JSON describing the PR and all active threads
- Local code and documentation updates
- Successful local build, test, lint, and pre-commit results when applicable
- A pushed commit containing the fixes
- One submitted review covering all active threads

## Supporting Reference

For the detailed operating procedure and output fields, read `references/workflow.md`.
