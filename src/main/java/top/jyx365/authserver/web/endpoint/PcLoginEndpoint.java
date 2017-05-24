package top.jyx365.authserver.web.endpoint;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.client.filter.state.DefaultStateKeyGenerator;
import org.springframework.security.oauth2.client.filter.state.StateKeyGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import top.jyx365.authserver.service.QrcodeAuthSessionService;
import top.jyx365.authserver.service.QrcodeAuthSessionService.QrcodeAuthSession;
import top.jyx365.utils.QRCodeUtil;

@Controller
@Slf4j
@RequestMapping("/wxlogin")
@SessionAttributes("uuid")
public class PcLoginEndpoint{

    @Autowired
    QrcodeAuthSessionService authSessionService;

    private StateKeyGenerator stateKeyGenerator = new DefaultStateKeyGenerator();

    @GetMapping("/entry/pc")
    public String weixinQRcodeLogin(Authentication auth,Model model, HttpServletRequest request) throws Exception {


        log.debug("weixinQRcodeLogin");
        QrcodeAuthSession session = authSessionService.newSession();

        String uuid = session.getUuid();

        String redirect_uri = UriComponentsBuilder
            .fromHttpUrl(request.getRequestURL().toString())
            .replacePath(request.getContextPath()+"/wxlogin/login/qrcode/callback")
            .queryParam("uuid",uuid)
            .build().encode().toUriString();

        model.addAttribute("weixinUrl", redirect_uri);
        model.addAttribute("weixinUrlQRcode", QRCodeUtil.createQRcodeBase64(redirect_uri));
        model.addAttribute("uuid", uuid);
        return "pc_login";
    }

    @GetMapping("/login/qrcode/callback")
    public String weixinQRcodeLoginShort(HttpServletRequest request,Model model,
            Authentication authentication )  {
        String uuid = request.getParameter("uuid");

        log.debug("weixinQRcodeLoginShort");
        QrcodeAuthSession session = authSessionService.getSession(uuid);

        log.debug("weixinAuthSession:{}",session);

        if(session == null) {
            model.addAttribute("message","二维码过期，请重新刷新登录页面");
            return "wx_login_fail";
        }

        session.setAuthentication(authentication);
        authSessionService.saveSession(session);

        return "wx_login_success";
    }
    @GetMapping("/entry/qrcode/check")
    @ResponseBody
    public Map<String,Object> weixinQRcodeLogin(
            @ModelAttribute("uuid")String uuid,
            HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String,Object>();
        if(uuid == null ) {
            result.put("status","error");
            result.put("message","uuid is null");
            return result;
        }
        QrcodeAuthSession session = authSessionService.getSession(uuid);
        if(session == null) {
            result.put("status","error");
            result.put("message","session timeout");
            return result;
        }
        log.debug("query weixinAuthSession:{}",session);

        Authentication auth = session.getAuthentication();
        if(auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            result.put("status","success");
        } else {
            result.put("status",false);
        }
        return result;
    }
}
