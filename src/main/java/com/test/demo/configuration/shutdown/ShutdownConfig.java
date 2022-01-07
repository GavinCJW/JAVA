package com.test.demo.configuration.shutdown;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfig {
    @Bean
    public Shutdown shutdown() {
        return new Shutdown();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final Shutdown shutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(shutdown);
        return factory;
    }
}
