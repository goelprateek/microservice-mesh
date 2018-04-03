package com.softcell.gonogo.gateway.config.apidocs;

import com.fasterxml.classmate.TypeResolver;
import com.softcell.gonogo.gateway.config.GoNoGoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Springfox Swagger configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In that
 * case, you can use a specific Spring profile for this class, so that only front-end developers
 * have access to the Swagger view.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";


    public GoNoGoProperties goNoGoProperties;


    public SwaggerConfiguration(GoNoGoProperties goNoGoProperties) {
        this.goNoGoProperties = goNoGoProperties;
    }

    @Bean
    PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        return new PageableParameterBuilderPlugin(nameExtractor, resolver);
    }


    @Bean
    public Docket swaggerSpringfoxApiDocket() {
        this.log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(this.goNoGoProperties.getSwagger().getContactName(),
                this.goNoGoProperties.getSwagger().getContactUrl(),
                this.goNoGoProperties.getSwagger().getContactEmail());

        ApiInfo apiInfo = new ApiInfo(this.goNoGoProperties.getSwagger().getTitle(),
                this.goNoGoProperties.getSwagger().getDescription(),
                this.goNoGoProperties.getSwagger().getVersion(),
                this.goNoGoProperties.getSwagger().getTermsOfServiceUrl(),
                contact, this.goNoGoProperties.getSwagger().getLicense(), this.goNoGoProperties.getSwagger().getLicenseUrl(), new ArrayList());

        Docket docket = (new Docket(DocumentationType.SWAGGER_2))
                .host(this.goNoGoProperties.getSwagger().getHost())
                .protocols(new HashSet(Arrays.asList(this.goNoGoProperties.getSwagger().getProtocols())))
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(new Class[]{ResponseEntity.class})
                .select()
                .paths(PathSelectors.regex(this.goNoGoProperties.getSwagger().getDefaultIncludePattern()))
                .build();

        watch.stop();
        this.log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }

}
