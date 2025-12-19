# Review Todo List

## Security

- [x] **Verify Security Testing**: `ApplicationConfig.java` now includes `@Profile("!test")`, which disables security configuration during tests. 
  
  **Mitigation**: Integration tests in the CI pipeline are configured to run with the `dev` profile, ensuring that security configurations are active and authentication/authorization are exercised. Additionally, dedicated integration tests exist for authentication and authorization endpoints, verifying access control in a non-test profile. See `/test/integration/security/AuthenticationIntegrationTest.java` for details.

## Dependencies

- [x] **Serenity/Cucumber Downgrade**: The downgrade was identified and corrected. Dependencies have been updated to their latest available versions: Serenity `4.3.4` and Cucumber `7.33.0`.
