package com.ukrsofttech.gcp.orchestration;

import com.google.api.client.util.Value;
import com.ukrsofttech.gcp.orchestration.api.DataFlowController;
import com.ukrsofttech.gcp.orchestration.api.RootContextController;
import com.ukrsofttech.gcp.orchestration.api.SwaggerOpenApiController;
import com.ukrsofttech.gcp.orchestration.config.DataFlowConfig;
import com.ukrsofttech.gcp.orchestration.config.SwaggerConfig;
import com.ukrsofttech.gcp.orchestration.config.ThreadConfig;
import com.ukrsofttech.gcp.orchestration.exception.DataFlowGovernanceExceptionHandler;
import com.ukrsofttech.gcp.orchestration.service.GcsAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Overall DataFlowGovernance application configuration and entry-point.
 *
 * @author Dmytro Maidaniuk
 */
@Configuration
@Import({
        DispatcherServletAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
//        ErrorMvcAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
//        JmxAutoConfiguration.class,
//        MultipartAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        DefaultErrorAttributes.class,
        DataFlowGovernanceExceptionHandler.class,
        ThreadConfig.class,
        // Swagger configuration
        Swagger2DocumentationConfiguration.class,
        InMemorySwaggerResourcesProvider.class,
        SwaggerOpenApiController.class,
        SwaggerConfig.class,
        // Application Configs
        DataFlowConfig.class,
        // Application services
        GcsAccessService.class,
        // Application Controllers
        RootContextController.class,
        DataFlowController.class
})
public class DataFlowGovernance extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(DataFlowGovernance.class);

    @Value("#{systemProperties['springfox.documentation.swagger.v2.path'] ?: '/api/docs'}")
    private String spelSomeDefault;

    public static void main(String[] args) {
        LOG.info("DataFlow Governance Application starting...");
        SpringApplication.run(DataFlowGovernance.class, args);
    }
}
