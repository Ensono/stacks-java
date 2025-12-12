# Review Todo List

## Security

- [ ] **Verify Security Testing**: `ApplicationConfig.java` now includes `@Profile("!test")`, which disables security configuration during tests. Ensure that integration tests running against deployed environments (e.g., in the pipeline) are NOT running with the `test` profile, or that there are specific tests covering authentication/authorization.

## Dependencies

- [x] **Serenity/Cucumber Downgrade**: The downgrade was identified and corrected. Dependencies have been updated to their latest available versions: Serenity `4.3.4` and Cucumber `7.33.0`.
