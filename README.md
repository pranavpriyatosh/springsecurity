# spring-security-handbook
spring-security features , code sample and documentations


# Security Use Cases
There are a number of places that you may want to go from here. To figure out what’s next for you and your application, consider these common use cases that Spring Security is built to address:

I am building a REST API, and I need to authenticate a JWT or other bearer token

I am building a Web Application, API Gateway, or BFF and

I need to login using OAuth 2.0 or OIDC

I need to login using SAML 2.0

I need to login using CAS

I need to manage

Users in LDAP or Active Directory, with Spring Data, or with JDBC

Passwords

In case none of those match what you are looking for, consider thinking about your application in the following order:

Protocol: First, consider the protocol your application will use to communicate. For servlet-based applications, Spring Security supports HTTP as well as Websockets.

Authentication: Next, consider how users will authenticate and if that authentication will be stateful or stateless

Authorization: Then, consider how you will determine what a user is authorized to do

Defense: Finally, integrate with Spring Security’s default protections and consider which additional protections you need
