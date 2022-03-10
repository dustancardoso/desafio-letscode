package com.desafioletscode.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;


@Configuration
@EnableAuthorizationServer
@EnableResourceServer
@EnableWebSecurity
@Profile(value = {"development", "production"})
public class ConfiguracaoSegurancaWeb extends WebSecurityConfigurerAdapter{
	@Autowired
	private JogadorUsuarioDetalhesService jUserService;	
	
    @Bean
    public TokenStore tokenStore() {
       return new InMemoryTokenStore();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jUserService).passwordEncoder(jUserService.getPasswordEncoder());
    }
    
}
