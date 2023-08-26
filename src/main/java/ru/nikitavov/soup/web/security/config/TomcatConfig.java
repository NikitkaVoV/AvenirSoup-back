package ru.nikitavov.soup.web.security.config;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Configuration
@RequiredArgsConstructor
public class TomcatConfig {

    @Value("${server.port}")
    private int httpPort;

    @Value("${server.ssl.key-store}")
    private String keystorePath;
    @Value("${server.ssl.key-store-type}")
    private String keystoreType;
    @Value("${server.ssl.enabled}")
    private boolean enable;
    @Value("${server.ssl.key-store-password}")
    private String keystorePassword;
    @Value("${server.ssl.key-alias}")
    private String keystoreAlies;
    @Value("${server.ssl.port}")
    private int httpsPort;

    private final ResourceLoader resourceLoader;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        if (enable) {
            // Настраиваем HTTPS коннектор
            tomcat.addAdditionalTomcatConnectors(
                    createHttpsConnector()
            );
        }

        return tomcat;
    }

    // Настраиваем HTTPS коннектор
    private Connector createHttpsConnector() {

        Resource resource = resourceLoader.getResource(keystorePath);

        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort(httpsPort);
        protocol.setSSLEnabled(true);
        protocol.setKeystoreType("PKCS12");
        protocol.setKeystoreFile(getFileFromResource(resource).getAbsolutePath());
        protocol.setKeystorePass(keystorePassword);
        protocol.setKeyAlias(keystoreAlies);
        return connector;
    }

    private File getFileFromResource(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            File tempFile = File.createTempFile("temp", ".tmp");
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
