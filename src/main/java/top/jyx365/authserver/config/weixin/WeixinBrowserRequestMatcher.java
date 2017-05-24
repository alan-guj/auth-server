package top.jyx365.authserver.config.weixin;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.util.matcher.RequestMatcher;


@Slf4j
public class WeixinBrowserRequestMatcher implements RequestMatcher {

    private String key = "micromessenger";

    public WeixinBrowserRequestMatcher() {}

    public WeixinBrowserRequestMatcher(String key) {
        this.key = key;
    }

    public boolean matches(HttpServletRequest  request) {
        boolean matchResult = false;
        String userAgent = request.getHeader("User-Agent");
        if(userAgent != null) matchResult = userAgent.toLowerCase().contains(key);
        return matchResult;
    }
}
