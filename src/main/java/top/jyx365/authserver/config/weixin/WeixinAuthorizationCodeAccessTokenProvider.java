package top.jyx365.authserver.config.weixin;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import org.springframework.util.MultiValueMap;

@Slf4j
public class WeixinAuthorizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {
/*
    @Override
    protected ResponseExtractor<OAuth2AccessToken> getResponseExtractor() {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(types);
        converters.add(converter);
        return new HttpMessageConverterExtractor<OAuth2AccessToken>(OAuth2AccessToken.class,converters);
    }
*/
    public WeixinAuthorizationCodeAccessTokenProvider() {
        super();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(types);
        converters.add(converter);
        super.setMessageConverters(converters);
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getAccessTokenUri(OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form) {
        String accessTokenUri = resource.getAccessTokenUri();
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(accessTokenUri);


        builder.queryParam("appid",resource.getClientId())
            .queryParam("secret",resource.getClientSecret())
            .queryParam("code",form.get("code").get(0))
            .queryParam("grant_type",form.get("grant_type").get(0));

        return builder.build().toUriString();
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details,AccessTokenRequest request)
        throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException,
        OAuth2AccessDeniedException
    {
        OAuth2AccessToken accessToken = super.obtainAccessToken(details, request);
        log.debug("obtainAccessToken accessToken:{}", accessToken.getAdditionalInformation());
        /*TODO: Add New User??*/
        return accessToken;
    }
    //protected OAuth2AccessToken retrieveToken(AccessTokenRequest request, OAuth2ProtectedResourceDetails resource,
            //MultiValueMap<String, String> form, HttpHeaders headers) throws OAuth2AccessDeniedException {

    //}
}
