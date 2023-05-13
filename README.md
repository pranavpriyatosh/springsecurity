# Spring Security with OAuth2.0 and Open ID connect
## Technology
#### Java11
#### Spring Boot 2.6.3
#### Spring data jpa for persistence<br/>
### We created one application with particular API’s & called authorization server. That particular API has to be authenticated by authorization server then only we will be able to access that API’s.Suppose, there are any API’s behind authorization server like Resource Server and we want to connect to the resources after authentication from our client that also handle by authorization server with Oauth2 & Open ID connect.<br/>
## OAuth2.0
#### OAuth2.0, stands for “Open Authorization”, it’s allow application to access resources hosted by other web apps on behalf of a user.
#### OAuth2.0 is an authorization protocol and NOT an authentication protocol.
#### It is designed to granting access to the resources. OAuth2.0 uses Access tokens. 
## Resource Owner
#### The user or system that owns the protected resources and grant access to them.
## Client
#### It is system that requires access to the protected resources. To access resources, client must have the appropriate Access Token.
## Authorization Server
#### It receives requests from the client for Access Token and issues them upon successful authentication and consent by the Resource Owner.
## Resource Server
#### A server that protects the user’s resources and receives access requests from Client. It accepts and validates an Access Token from the Client and returns the appropriate resources to it
## OAuth2.0 Scope
#### Scopes are used to specify exactly the reason for which access to resources may be granted.<br/><br/>

![Abstract Protocol Flow](https://assets.digitalocean.com/articles/oauth/abstract_flow.png)
#### Here is a more detailed explanation of the steps in the diagram:
1.	The application requests authorization to access service resources from the user
2.  If the user authorized the request, the application receives an authorization grant
3.	The application requests an access token from the authorization server (API) by presenting authentication of its own identity, and the authorization grant
4.	If the application identity is authenticated and the authorization grant is valid, the authorization server (API) issues an access token to the application. Authorization is complete.
5.	The application requests the resource from the resource server (API) and presents the access token for authentication
6.	If the access token is valid, the resource server (API) serves the resource to the application
## Step to be follow:
### Application (Client) Registration
#### Before using OAuth with your application, you must register your application with the service. This is done through a registration form in the developer or API portion of the service’s website, where you will provide the following information:
- Application Name
- Application Website
- Redirect URI or Callback URL
#### The redirect URI is where the service will redirect the user after they authorize (or deny) your application, and therefore the part of your application that will handle authorization codes or access tokens.
<span style="color:#0969DA">
After registration of your client you will get Client ID & Client Secret
</span> 

### Client ID and Client Secret
#### Once your application is registered, the service will issue client credentials in the form of a client identifier and a client secret. The Client ID is a publicly exposed string that is used by the service API to identify the application, and is also used to build authorization URLs that are presented to users. The Client Secret is used to authenticate the identity of the application to the service API when the application requests to access a user’s account, and must be kept private between the application and the API.

### Authorization Grant
#### The authorization grant type depends on the method used by the application to request authorization, and the grant type supported by the API.
####
<span style="color:#0969DA">
In this project we are using Authorization Code Grant Type.
</span> 

#### Authorization Code Grant Type is used with server side application, where source code is not publicly exposed, and Client Secrete confidentiality can be maintained.
### Authorization Code Flow:
1. Authorization URL
#### Before authorization begins, it first generates a random string to use for the state parameter. The Client will need to store this Response_type, client_id, redirect_uri, scope, state to be used in next step.

2. Verify state parameter
#### Does the state store by the client match the state in redirect?
3. Exchange the Authorization Code
4. Token Endpoint Response
#### The response includes the access token & refresh token
## Oauth2 spring security tutorial contains:
### Oauth-authorization-server
###   Oauth-resource-server
###   Spring-security-client<br/><br/>
<span style="color:#0969DA">

## Oauth-authorization-server
</span> 

### Dependency

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-oauth2-authorization-server</artifactId>
    <version>0.2.2</version>
</dependency>
```

### Application.properties file
```
server:
  port: 9000

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_registration
    username: root
    password: Sandhya@123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
```
### CustomUserDetailsService Class
```
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw  new UsernameNotFoundException("No User Found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(List.of(user.getRole()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority>  authorities = new ArrayList<>();
        for(String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}

```
- CustomUserDetailsService is a class which implements UserDetailsService interface.
- UserDetailsService is the default service provided for user to interact with spring security to check if user is exist or not.
- In this class loadUserByUsername() method is override to load user from database & send to the spring security.
- If user = null then throws UserNameNotFoundException. Else it will create user object with granted authorities.
<br/><br/>
### Configuration
### AuthorizationServerConfig
```
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        return http.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("api-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/api-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("api.read")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();


        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://auth-server:9000")
                .build();
    }
}
```
- Here, we implement SecurityFilterChain which injecting HttpSecurity
- We are applying default security with applyDefaultSecurity(http) method
- We implement RegisteredClientRepository which is used to register all clients.
- We have only one client in this project i.e. **spring-security-client**
- So, in registeredClientRepository() method we are providing Client details like clientId, clientSecret, authorizationGrantType. 

<span style="color:red"> 
Note : 
You need to define your authorization server URL "http://auth-server:9000" into the host file.
</span>
 
### DefaultServerConfig
```
@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Autowired
    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder
                .authenticationProvider(customAuthenticationProvider);
    }
}
```

### CustomAuthenticationProvider

```
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user= customUserDetailsService.loadUserByUsername(username);
        return checkPassword(user,password);
    }

    private Authentication checkPassword(UserDetails user, String rawPassword) {
        if(passwordEncoder.matches(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        }
        else {
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```
- CustomAuthenticationProvider implements AuthenticationProvider interface.
- This class override authenticate() method of AuthenticationProvider interface to authenticate user.


<span style="color:#0969DA">

## Oauth-resource-server
</span> 

### Dependency
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
### Application.properties file
```
server:
  port: 8090

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://auth-server:9000
```

### Configuration
### ResourceServerConfig class
```
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/api/**")
                .access("hasAuthority('SCOPE_api.read')")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}

```

<span style="color:#0969DA">

## Spring-security-client
</span>

### Dependency
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<!--Dependencies for WebClientConfiguration-->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webflux</artifactId>
</dependency>
<dependency>
	<groupId>io.projectreactor.netty</groupId>
	<artifactId>reactor-netty</artifactId>
</dependency>
```
### Application.properties file
```
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_registration
    username: root
    password: Sandhya@123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
  security:
    oauth2:
      client:
        registration:
          api-client-oidc:
            provider: spring
            client-id: api-client
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "http://127.0.0.1:8080/login/oauth2/code/{registrationId}"
            scope: openid
            client-name: api-client-oidc
          api-client-authorization-code:
            provider: spring
            client-id: api-client
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "http://127.0.0.1:8080/authorized"
            scope: api.read
            client-name: api-client-authorization-code
        provider:
          spring:
            issuer-uri: http://auth-server:9000

```
### Configuration
### WebClientConfiguration
```
@Configuration
public class WebClientConfiguration {

    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}

```
- We use webClient to called that particular APIs which are added in Resource Server

## Steps to Run
1. Build Oauth-authorization-server
2. Build Oauth-resource-server
3. Build spring-security-client
4. Run Oauth-authorization-server
5. Run Oauth-resource-server
6. Run spring-security-client
7. The web application is accessible via [localhost:8080]()
8. It will redirect to Login page. Enter [username]() & [password]() to authorize client.
9. It will ask for the consent from resource server. Select api.read consent & click on [Submit]() button.
10. Then you will get the required data from database.

