package top.jyx365.authserver.security;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import top.jyx365.authserver.repository.ClientRepository;
import top.jyx365.authserver.domain.Client;


/**
 * Service compatible with spring Oauth {@link ClientDetailsService} that checks client against
 * our database implementation.
 */
@Service
@Slf4j
public class DatabaseClientDetailsService implements ClientDetailsService{


    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug("loadClientByClientId:{}", clientId);
        if(clientId == null){
            log.info("clientId '"+clientId+"' is invalid");
            return null;
        }
        Client client = clientRepository.findOne(clientId);
        if(client == null){
            log.info("clientId '"+clientId+"' doesn't exist");
            return null;
        }

        return new DatabaseClientDetailsAdapter(client);
    }
}
