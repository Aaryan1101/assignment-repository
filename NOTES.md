# Patch Notes

## Summary

- Corrected SQL boolean precedence so the archived, search-term, and status conditions apply to every result. I made the equivalent correction in both SQL reference artifacts.
- Moved pagination into the repository query with `Pageable` instead of loading every match and slicing it in memory. Added bounds checks for page parameters and a clear 400 response for unsupported statuses.
- Removed an artificial request delay that made short and empty searches take up to one second.
- Made frontend searches wait briefly while the user types and cancel obsolete requests. This prevents excessive calls and older responses overwriting newer results. Error/loading state is now reset consistently.
- Reset pagination when search or status changes, preventing valid filtered results from appearing empty because the UI remained on a later page.
- Contained the task table in an accessible horizontal scroll region and stacked filters on narrow screens so columns and controls are no longer clipped on phones.
- Added backend regression tests for query correctness, pagination, and invalid request parameters.

## Not changed

I did not redesign the UI, change the schema, or modernize dependencies because those were outside a focused patch. The Oracle package was corrected by inspection but not executed because Oracle is not in the local setup.

## Biggest remaining risk

Search still uses `LIKE '%term%'`. This suits the small dataset but will not scale well because a normal index cannot efficiently serve it. Production should evaluate full-text search and representative load tests.

## Tools used

I used OpenAI Codex to inspect the repository, help implement the patch and tests, and automate browser smoke testing. I reviewed the resulting changes and kept the final scope focused.
