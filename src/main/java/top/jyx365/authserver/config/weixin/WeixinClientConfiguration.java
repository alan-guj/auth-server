package top.jyx365.authserver.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CompositeFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import top.jyx365.authserver.config.weixin.WeixinAuthorizationCodeAccessTokenProvider;
import top.jyx365.authserver.config.weixin.WeixinBrowserRequestMatcher;
import top.jyx365.authserver.config.weixin.WeixinOAuth2ClientContextFilter;
import top.jyx365.authserver.service.UserService;
import top.jyx365.authserver.service.weixin.WeixinUserInfoTokenServices;


@EnableOAuth2Client
@Configuration
@Order(99)
public class WeixinClientConfiguration extends WebSecurityConfigurerAdapter{

    @Configuration
    public static class WeixinOAuth2RestTemplateConfiguration {

        @Autowired
        private ClientResources client;

        @Autowired
        private OAuth2ClientContext oauth2ClientContext;

        @Bean
        public OAuth2RestTemplate weixinClientRestTemplate() {
            OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
            template.setMessageConverters(getMessageConverter());
            WeixinAuthorizationCodeAccessTokenProvider accessTokenProvider = new WeixinAuthorizationCodeAccessTokenProvider();
            template.setAccessTokenProvider(accessTokenProvider);
            return template;
        }

        private List<HttpMessageConverter<?>> getMessageConverter() {
            List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            List<MediaType> types = new ArrayList<MediaType>();
            types.add(MediaType.TEXT_PLAIN);
            converter.setSupportedMediaTypes(types);
            converters.add(converter);
            return converters;
        }

    }


    @Configuration
    public static class ClientResourcesConfiguration {
        @Bean
        @ConfigurationProperties("weixin")
        public ClientResources weixin() {
            return new ClientResources();
        }
    }

    public static class ClientResources {

        @NestedConfigurationProperty
        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

        @NestedConfigurationProperty
        private ResourceServerProperties resource = new ResourceServerProperties();

        public AuthorizationCodeResourceDetails getClient() {
            return client;
        }

        public ResourceServerProperties getResource() {
            return resource;
        }
    }

    //@Autowired
    //private ResourceServerTokenServices tokenServices;
    @Autowired
    @Qualifier("weixinClientRestTemplate")
    private OAuth2RestTemplate template;

    @Autowired
    ClientResources weixin;
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        LoginUrlAuthenticationEntryPoint pcEntry =
            new LoginUrlAuthenticationEntryPoint("/wxlogin/entry/pc");
        pcEntry.setUseForward(true);
        LoginUrlAuthenticationEntryPoint weixinEntry =
            new LoginUrlAuthenticationEntryPoint("/wxlogin/entry/weixin");


        http.headers().frameOptions().disable()
            .and().requestMatchers()
                    .antMatchers("/wxlogin/**","/oauth/**")
                    //.antMatchers("/**")
                    .and().authorizeRequests()
                    //.antMatchers("/api/**")
                        //.access("hasAnyAuthority('ROLE_SYSTEM','ROLE_SYSTEM_DEVELOPER')")
                    .antMatchers(
                            "/wxlogin/entry/**")
                        .permitAll()
                    .anyRequest().authenticated()
            .and().exceptionHandling()
                .defaultAuthenticationEntryPointFor(weixinEntry,new WeixinBrowserRequestMatcher())
                .defaultAuthenticationEntryPointFor(pcEntry,AnyRequestMatcher.INSTANCE)
            .and().logout()
                .logoutSuccessUrl("/").permitAll()
            .and().csrf().disable()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //.and()
                .addFilterBefore(weixinSsoFilter(),BasicAuthenticationFilter.class);
    }

    @Bean
    public OAuth2ClientContextFilter oauth2ClientContextFilter() {
        return new WeixinOAuth2ClientContextFilter();
    }

    private Filter ssoFilter(String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                path);
        WeixinUserInfoTokenServices tokenServices = new WeixinUserInfoTokenServices(
                weixin, template , userService);
        filter.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        return filter;
    }


    @Bean
    public Filter weixinSsoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter("/wxlogin/entry/weixin"));
        filter.setFilters(filters);
        return filter;
    }


}
