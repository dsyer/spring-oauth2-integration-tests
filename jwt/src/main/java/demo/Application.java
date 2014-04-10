package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.JwtTokenEnhancer;
import org.springframework.security.oauth2.provider.token.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {
		
		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.tokenStore(tokenStore);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				.authorizeRequests()
				.anyRequest().authenticated();
			// @formatter:on
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private ClientDetailsService clientDetailsService;

		@Bean
		public JwtTokenStore tokenStore() {
			JwtTokenStore store = new JwtTokenStore(tokenEnhancer());
			return store;
		}
		
		@Bean
		public JwtTokenEnhancer tokenEnhancer() {
			return new JwtTokenEnhancer();
		}
		
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.authenticationManager(authenticationManager).tokenStore(tokenStore()).tokenEnhancer(tokenEnhancer());
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
		 	clients.inMemory()
		        .withClient("my-trusted-client")
		            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
		            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
		            .scopes("read", "write", "trust")
		            .accessTokenValiditySeconds(60)
 		    .and()
		        .withClient("my-client-with-registered-redirect")
		            .authorizedGrantTypes("authorization_code")
		            .authorities("ROLE_CLIENT")
		            .scopes("read", "trust")
		            .redirectUris("http://anywhere?key=value")
 		    .and()
		        .withClient("my-client-with-secret")
		            .authorizedGrantTypes("client_credentials", "password")
		            .authorities("ROLE_CLIENT")
		            .scopes("read", "write")
		            .secret("secret");
		// @formatter:on
		}

	}

}
