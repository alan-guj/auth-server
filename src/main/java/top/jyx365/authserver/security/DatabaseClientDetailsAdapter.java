package top.jyx365.authserver.security;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;
import top.jyx365.authserver.domain.Client;

/**
 * This class serves as an adapter od the client entity to the ClientDetails class that
 * spring oauth2 is expecting.
 */

@Slf4j
public class DatabaseClientDetailsAdapter implements ClientDetails {

    private Client client;

    public DatabaseClientDetailsAdapter(Client client){
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        log.debug("D");
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return client.isSecretRequired();
    }

    @Override
    public String getClientSecret() {
        return client.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return getScope()!= null && !getScope().isEmpty();
    }

    @Override
    public Set<String> getScope() {
        return client.getScope();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return client.getAuthorizedGrantTypes();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return client.getRegisteredRedirectUri();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        //Clients only have a auth_client role
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM"));
        return authorities;
        //return client.getUser().getGrantedAuthority();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenValiditySeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return client.getRefreshTokenValiditySeconds();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        // TODO this should be improved is a placeholder.
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        log.debug("A");
        return null;
    }
}
