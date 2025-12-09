#!/usr/bin/env bash
set -euo pipefail

ALLOWED_TAGS=${1:-""}
IGNORED_TAGS=${2:-""}

trim() {
  local value="${1:-}"
  # shellcheck disable=SC2001
  value=$(printf '%s' "$value" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')
  printf '%s' "$value"
}

to_cucumber_tags() {
  local raw="$1"
  local -n target_ref=$2

  IFS='|' read -r -a entries <<<"$raw"
  for entry in "${entries[@]}"; do
    local cleaned
    cleaned=$(trim "$entry")
    [[ -z "$cleaned" ]] && continue
    [[ "$cleaned" == @* ]] || cleaned="@$cleaned"
    target_ref+=("$cleaned")
  done
}

declare -a allowed=()
declare -a ignored=()

to_cucumber_tags "$ALLOWED_TAGS" allowed
to_cucumber_tags "$IGNORED_TAGS" ignored

output=""
if (( ${#allowed[@]} > 0 )); then
  output=$(printf '%s' "${allowed[0]}")
  for idx in "${allowed[@]:1}"; do
    output+=" or $idx"
  done
fi

if (( ${#ignored[@]} > 0 )); then
  for tag in "${ignored[@]}"; do
    if [[ -n "$output" ]]; then
      output+=" and not $tag"
    else
      output+="not $tag"
    fi
  done
fi

printf '%s\n' "$output"
