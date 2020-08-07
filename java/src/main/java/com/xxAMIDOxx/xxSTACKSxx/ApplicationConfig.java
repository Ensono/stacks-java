package com.xxAMIDOxx.xxSTACKSxx;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Arrays;


@Configuration
@EnableWebSecurity(debug = true)
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;

  /**
   * Provide CorsConfiguration for each request
   *
   * @return */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

  /**
   * Configure your API to use the RS256 and protect and API endpoints.
   *
   * /api/public: available for non-authenticated requests.
   * /api/private: available for authenticated requests containing an Access-Token with no additional scopes.
   * /api/private-scoped: available for authenticated requests containing an Access-Token with the
   * read:messages scope granted
   *
   * The hasAuthority() method provides a way to specify the required scope.
   *
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        JwtWebSecurityConfigurer
                .forRS256(apiAudience, issuer)
                .configure(http)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/v1/menu").authenticated()
                .antMatchers(HttpMethod.DELETE, "/v1/menu/{id}").authenticated()
                .antMatchers(HttpMethod.POST, "/v1/menu").hasAuthority("create:menu");
    }

}


