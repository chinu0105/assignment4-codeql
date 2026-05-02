# Architecture — CodeQL Security Scanning

## Why Two Workflows?

I deliberately kept both standard and advanced CodeQL.
They are not redundant — they cover different things:

## Standard CodeQL (codeql.yml)
- Uses `autobuild` — compiles the code first
- CodeQL traces actual dataflow through compiled 
  bytecode
- Catches deeper injection paths that static-only 
  analysis misses
- More accurate Java vulnerability detection

## Advanced CodeQL (codeql-advanced.yml)  
- Scans GitHub Actions YAML files for script injection
- Runs on a weekly schedule (cron) — catches newly 
  disclosed CVEs against unchanged code
- Uses CodeQL v4 with build-mode: none — faster
- Covers `java-kotlin` + `actions` language targets

## Why I Added Vulnerable Code
Commit d4c8797 intentionally introduces a vulnerable 
pattern to validate CodeQL actually detects it.
Configuring a SAST tool without validating it catches 
real issues is a false sense of security.

## Query Suites
- security-extended: CVEs, injection, crypto weakness
- security-and-quality: above + null dereference, 
  resource leaks, dead code, API misuse

## What a Senior Engineer Knows
CodeQL builds a semantic database of compiled code —
not text scanning. It models dataflow from source to 
sink. That's why it catches SQL injection that 
grep-based tools miss entirely.

## What I Would Add in Production
- Branch protection: block merge on any Critical finding
- Custom .codeql config file with path exclusions
- SARIF results exported to security dashboard
- Separate workflow for dependency scanning (Dependabot)
