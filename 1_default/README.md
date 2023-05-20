By default adding spring security dependency in classpath, springboot configure Auth security and consider credetinal in application.yml for authentication.

The default arrangement of Spring Boot and Spring Security affords the following behaviors at runtime:​

Requires an authenticated user for any endpoint (including Boot’s /error endpoint)​

Registers a default user with a generated password at startup (the password is logged to the console; in the preceding example, the password is 8e557245-73e2-4286-969a-ff57fe326336)​

Protects password storage with BCrypt as well as others​

Provides form-based login and logout flows​

Authenticates form-based login as well as HTTP Basic​

Provides content negotiation; for web requests, redirects to the login page; for service requests, returns a 401 Unauthorized​

Mitigates CSRF attacks​

Mitigates Session Fixation attacks​

Writes Strict-Transport-Security to ensure HTTPS​

Writes X-Content-Type-Options to mitigate sniffing attacks​

Writes Cache Control headers that protect authenticated resources​

Writes X-Frame-Options to mitigate Clickjacking​

Integrates with HttpServletRequest's authentication methods​

Publishes authentication success and failure events
