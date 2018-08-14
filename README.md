# spring-data-redis-lettuce
Spring Data Redis Lettuce factory configuration is a project definig the easy way to connect and use Redis using Spring data.

# Define diferent timeout values (socket timeout versus paramenters timeout)


Default socket timeout is 10 seconds
Default command timeout is 60 seconds

We define lettuce connection factory with custom socket and command timeout:

    @Bean
       LettuceConnectionFactory lettuceConnectionFactory() {
   
           final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(socketTimeout).build();
           final ClientOptions clientOptions =
                   ClientOptions.builder().socketOptions(socketOptions).build();
   
           LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                   .commandTimeout(redisCommandTimeout)
                   .clientOptions(clientOptions).build();
           RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost,
                   redisPort);
   
           final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig,
                   clientConfig);
           lettuceConnectionFactory.setValidateConnection(true);
           return new LettuceConnectionFactory(serverConfig, clientConfig);
   
   
       }

![Timeout configuration](/image/timeout_configuration.png?raw=true "Timeout configuration]")




# Use Duration class to be able to define Timeouts

Spring Boot has dedicated support for expressing durations. If you expose a java.time.Duration property, the following formats in application properties are available:


* A regular long representation (using milliseconds as the default unit unless a @DurationUnit has been specified)
* A more readable format where the value and the unit are coupled (e.g. 10s means 10 seconds)
* The standard ISO-8601 format used by java.util.Duration



The Duration in the moment (Spring-Boot 2.0.4.RELEASE) it is not possible to use together with @Value notation, but it is possible to use with @ConfigurationProperties.

You can provide a **ConversionService** bean (**with a bean named conversionService**).
In Spring data redis lettuce project conversionService was defined in **ConversionConfiguration** class (**package com.cromero.redis.configuration**)

    // to be able to use @Value Spring boot 2 property conversion. They were expecting this feature out of the box for pring-boot 2.1.0.    
    //https://docs.spring.io/spring-boot/docs/2.0.4RELEASE/reference/htmlsingle/#boot-features-external-config-conversion-duration
    
    @Bean
    public ApplicationConversionService conversionService()
    {
        final ApplicationConversionService applicationConversionService = new ApplicationConversionService();
        return applicationConversionService;
    }


Spring oficial documentation:

https://docs.spring.io/spring-boot/docs/2.0.4.RELEASE/reference/htmlsingle/#boot-features-external-config-conversion

Spring Boot attempts to coerce the external application properties to the right type when it binds to the @ConfigurationProperties beans. If you need custom type conversion, you can provide a ConversionService bean (with a bean named conversionService) or custom property editors (through a CustomEditorConfigurer bean) or custom Converters (with bean definitions annotated as @ConfigurationPropertiesBinding).

[Note]

As this bean is requested very early during the application lifecycle, make sure to limit the dependencies that your ConversionService is using. Typically, any dependency that you require may not be fully initialized at creation time. You may want to rename your custom ConversionService if it is not required for configuration keys coercion and only rely on custom converters qualified with @ConfigurationPropertiesBinding.
