package com.cromero.redis.configuration;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.host}")
    private String redisHost;

    private @Value("${spring.redis.port}")
    int redisPort;

    private @Value("${spring.redis.timeout}")
    Duration redisCommandTimeout;

    private @Value("${spring.redis.custom.socket.timeout}")
    Duration socketTimeout;



    @Bean
    public StringRedisTemplate redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        final StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }



    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(socketTimeout).build();
        final ClientOptions clientOptions =
                ClientOptions.builder().socketOptions(socketOptions).build();
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions).build();
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost,
                redisPort);
        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig,
                clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        return new LettuceConnectionFactory(serverConfig, clientConfig);


    }

}
