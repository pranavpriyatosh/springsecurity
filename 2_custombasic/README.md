# Spring Security Basic customization

We can configure what endpoint to be secured by spring security and which should be left unsecured by simply defining a SecurityFilterChain bean in app context. Here we say **secure** the /myAccount","/myBalance","/myLoans","/myCards" apis of typical bank app and allow all to access "/notices","/contact" api.

 @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        /**
         *  Below is the custom security configurations
         */

        http.authorizeHttpRequests()
                        .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
                        .requestMatchers("/notices","/contact").permitAll()
                .and().formLogin()
                .and().httpBasic();
        return http.build();
 }
