package demo.simplemarketplace;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Simple MarketPlace APIs")
        .description(
            "This is the Global MarketPlace Server supporting the access and management of the resources that it governs. ")
        .license("")
        .licenseUrl("")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("", "", ""))
        .build();
  }

  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .directModelSubstitute(LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo());
  }
}
