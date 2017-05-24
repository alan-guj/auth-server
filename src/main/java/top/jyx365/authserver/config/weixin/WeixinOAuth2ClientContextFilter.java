package top.jyx365.authserver.config.weixin;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class WeixinOAuth2ClientContextFilter extends OAuth2ClientContextFilter {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void redirectUser(UserRedirectRequiredException e,
            HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        String redirectUri = e.getRedirectUri();
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(redirectUri);
        Map<String, String> requestParams = e.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            if("client_id".equals(param.getKey())) {
                builder.queryParam("appid", param.getValue());
            } else if("redirect_uri".equals(param.getKey())) {
                //builder.queryParam(param.getKey(), URLEncoder.encode(param.getValue(),"UTF-8"));
                builder.queryParam(param.getKey(), param.getValue());
            } else {
                builder.queryParam(param.getKey(), param.getValue());
            }
        }

        if (e.getStateKey() != null) {
            builder.queryParam("state", e.getStateKey());
        }

        String redirect = builder.build().encode().toUriString()+"#wechat_redirect";
        log.info("weixinredirect:{}",redirect);

        this.redirectStrategy.sendRedirect(request, response, redirect);
    }
}
