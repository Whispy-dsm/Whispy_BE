# Notepad
<!-- Auto-managed by OMC. Manual edits preserved in MANUAL section. -->

## Priority Context
<!-- ALWAYS loaded. Keep under 500 chars. Critical discoveries only. -->

## Working Memory
<!-- Session notes. Auto-pruned after 7 days. -->
### 2026-02-20 10:55
OAuth mobile deep-link flow: success now stores one-time Redis code (oauth:code:{uuid}, TTL 3m), failure redirects with error/error_description, and /users/oauth/exchange consumes code via getAndDelete then issues JWT.


## MANUAL
<!-- User content. Never auto-pruned. -->

