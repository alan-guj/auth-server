package top.jyx365.authserver.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import top.jyx365.amqp.annotation.PublishMessage;

import top.jyx365.authserver.domain.Client;
import top.jyx365.authserver.domain.User;
import top.jyx365.authserver.domain.User.UserCompany;
import top.jyx365.authserver.repository.ClientRepository;
import top.jyx365.authserver.service.UserService;
import top.jyx365.authserver.service.UserService.*;

@RestController
@Slf4j
@RequestMapping("/api/v2/users")
public class UserController {


    @Autowired
    UserService userService;


    @GetMapping("current")
    public Map<String,Object> userv2(OAuth2Authentication user) {
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("user", user.getPrincipal());
        map.put("authorities", user.getAuthorities());
        map.put("details", user);
        return map;
    }

}


@RestController
@Slf4j
@RequestMapping("/api/v1.1/users")
class UserControllerV1_1 extends UserControllerV1 {

    @Autowired
    private UserService userService;

    @RequestMapping(path="{userId}", method = RequestMethod.PUT)
    @PublishMessage
    @Override
    public UserInfo updateUserInfoV1(@PathVariable Long userId,@RequestBody UserInfo userInfo) {
        User user = userService.getUserById(userId);
        if(user == null) throw new ResourceNotFoundException(userId.toString());
        //if(user == null) throw new Exception(userId.toString());

        user.setMobile(userInfo.getMobile());
        user.setNickname(userInfo.getNickname());
        user.setFirstName(userInfo.getName());
        user.setImageUrl(userInfo.getPortrait_uri());
        user.setEmail(userInfo.getEmail());
        user.setMobile(userInfo.getMobile());
        if(userInfo.getCompany_id() != null) {
            Set<UserCompany> userCompanies = new HashSet<UserCompany>();
            userCompanies.add(
                    new UserCompany(userInfo.getCompany_id(),userInfo.getStaff_id()));
            user.setUserCompanies(userCompanies);
        }


        return UserInfo.convert(userService.save(user));
    }

}

@RestController
@Slf4j
@RequestMapping("/api/v1.0/users")
class UserControllerV1 {


    @Autowired
    private UserService userService;

    @Autowired
    private ClientRepository clientDao;


    //@GetMapping(value = {"check_token"})
    //public Map<String,Object> userv1(OAuth2Authentication auth) {
        //Map<String,Object> map = new LinkedHashMap<String,Object>();
        ////Map<String,Object> userInfo = new LinkedHashMap<String,Object>();
        ////UserInfo userInfo = new UserInfo();
        //try {
            //User user = (User)auth.getPrincipal();
            //UserInfo userInfo = UserInfo.convert(user);
            //map.put("user", userInfo);
            //map.put("authorities", userInfo.getType());
            //return map;
        //} catch( Exception ex ) {
            //String clientId = (String)auth.getPrincipal();
            //Client client = clientDao.findOne(clientId);
            //User clientUser = client.getUser();
            ////Map<String,Object> client = new HashMap<String,Object>();
            ////client.put("name",(String)auth.getPrincipal());
            //if(clientUser == null) {
               //clientUser = new User();
               //clientUser.setName(clientId);
            //}
            //UserInfo clientUserinfo = UserInfo.convert(clientUser);
            //clientUserinfo.setType("system_service");
            //map.put("user",clientUserinfo);
            //map.put("client",client);
            //map.put("auth",auth);
            //map.put("authorities","system_service");
            //return map;
        //}
    //}


    @GetMapping(value = {"current","check_token"})
    public Map<String,Object> currentUserv1(OAuth2Authentication auth) {
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        //Map<String,Object> userInfo = new LinkedHashMap<String,Object>();
        //UserInfo userInfo = new UserInfo();
        String principal = (String)auth.getPrincipal();
        try {
            Long userId = Long.valueOf(auth.getPrincipal().toString());
            User user = userService.getUserWithAuthorities(userId);
            UserInfo userInfo = UserInfo.convert(user);
            map.put("user", userInfo);
            map.put("authorities", userInfo.getType());
            return map;
        } catch( NumberFormatException ex ) {
            String clientId = (String)auth.getPrincipal();
            Client client = clientDao.findOne(clientId);
            User clientUser = client.getUser();
            //Map<String,Object> client = new HashMap<String,Object>();
            //client.put("name",(String)auth.getPrincipal());
            if(clientUser == null) {
                clientUser = new User();
                clientUser.setFirstName(clientId);
            }
            UserInfo clientUserinfo = UserInfo.convert(clientUser);
            clientUserinfo.setType("system_service");
            map.put("user",clientUserinfo);
            map.put("client",client);
            map.put("auth",auth);
            map.put("authorities","system_service");
            return map;
        }
    }


    @RequestMapping(value = "registration", method = RequestMethod.POST)
    @PublishMessage
    public UserInfo userRegistrationv1(OAuth2Authentication auth, @RequestBody Map<String,String> registrationInfo)
    throws Exception {
        if(registrationInfo.get("mobile") == null )  {
            log.debug("mobile cannot be null");
            throw new HttpRequestMethodNotSupportedException("/api/v1.0/users/registration", "mobile needed");
        }

        User user = (User)auth.getPrincipal();
        user = userService.getUserById(user.getId());

        String mobile = registrationInfo.get("mobile");
        String enterprise_id = registrationInfo.get("enterprise_id");

        //user = userService.registrationv1(user, mobile, enterprise_id);

        user.setMobile(mobile);
        user.getUserCompanies().add(new UserCompany(enterprise_id,null));

        userService.save(user);

        /*Update sotred tokens*/
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user, "N/A", user.getGrantedAuthorities());

        OAuth2Authentication newAuth = new OAuth2Authentication(auth.getOAuth2Request(), token);

        //tokenService.updateAuthentication(auth,newAuth);

        return UserInfo.convert(user);

    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object>  queryUserInfoV1(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String openid,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role
            )
    {
        Map<String, Object> ret = new HashMap<String,Object>();

        User user = new User();
        if(id !=null) user.setId(Long.valueOf(id));
        user.setMobile(mobile);
        user.setOpenid(openid);
        user.setFirstName(name);
        ret.put("users",userService.findUsers(user)
            .stream()
            .map(UserInfo::convert)
            .collect(Collectors.toList())
            );
        return ret;
    }

    @RequestMapping( value = "{userId}", method = RequestMethod.GET )
    public Map<String, Object>  getUserInfo(@PathVariable Long userId)  {
        Map<String, Object> ret = new HashMap<String,Object>();
        User user = userService.getUserWithAuthorities(userId);
        List<UserInfo> users = new ArrayList<UserInfo>();
        if(user != null) {
            users.add(UserInfo.convert(user));
        }
        ret.put("users", users);
        return ret;
        //else throw new ResourceNotFoundException(userId.toString());
        //else throw new Exception(userId.toString());
    }

    @RequestMapping(path="{userId}", method = RequestMethod.PUT)
    @PublishMessage
    public UserInfo updateUserInfoV1(@PathVariable Long userId,@RequestBody UserInfo userInfo) {
        User user = userService.getUserById(userId);
        //if(user == null) throw new ResourceNotFoundException(userId.toString());
        //if(user == null) throw new Exception(userId.toString());

        if(userInfo.getMobile()!=null) user.setMobile(userInfo.getMobile());
        if(userInfo.getCompany_id() != null) {
            user.getUserCompanies().add(new UserCompany(userInfo.getCompany_id(),null));
        }

        if(userInfo.getName() != null) user.setFirstName(userInfo.getName());

        return UserInfo.convert(userService.save(user));
    }
}
