package top.jyx365.authserver.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.springframework.security.core.Authentication;

@Service
public class QrcodeAuthSessionService {

    private final String PREFIX="qrcodeauthsession:";


    @Getter
    @Setter
    @ToString
    public static class QrcodeAuthSession implements Serializable {

        private static final long serialVersionUID = -8336929034533774130L;

        private String uuid;
        private Authentication authentication;
        private String queryString;

        public QrcodeAuthSession() {

        }

        public QrcodeAuthSession(String uuid) {
            this.uuid = uuid;
        }

    }

    @Configuration
    public static class QrcodeAuthSessionRedisConfiguration {

        @Bean
        RedisTemplate<String, QrcodeAuthSession> qrcodeAuthSessionRedisTemplate(RedisConnectionFactory factory) {
            RedisTemplate<String, QrcodeAuthSession> template = new RedisTemplate<String, QrcodeAuthSession>();
            template.setConnectionFactory(factory);
            template.setKeySerializer(new StringRedisSerializer());
            //template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            return template;
        }
    }

    @Autowired
    @Qualifier("qrcodeAuthSessionRedisTemplate")
    private RedisTemplate<String, QrcodeAuthSession> template;

    public QrcodeAuthSession getSession(String uuid) {
        return template.opsForValue().get(PREFIX+uuid);
    }

    public QrcodeAuthSession newSession() {
        String uuid = UUID.randomUUID().toString();
        QrcodeAuthSession session = new QrcodeAuthSession(uuid);

        template.opsForValue().set(PREFIX+uuid,session,300,TimeUnit.SECONDS);

        return session ;
    }

    public void saveSession(QrcodeAuthSession session) {
        if(session != null && session.getUuid() != null)
            template.opsForValue().set(PREFIX+session.getUuid(),session,300,TimeUnit.SECONDS);
    }
}
