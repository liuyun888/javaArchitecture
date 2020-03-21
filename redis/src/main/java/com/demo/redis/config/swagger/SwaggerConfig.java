package com.demo.redis.config.swagger;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: javaArchitecture
 * @description: 使用注解启动swagger
 * @author: LiuYunKai
 * @create: 2020-03-05 23:15
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket createRestApi(){

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.demo.gis.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                //页面标题
                .title("SpringBoot 使用 Swagger2 构建 RESTful Api")
                //创建人
                .contact(new Contact("HanEr","https://www.toutiao.com/i6799277501970383373/",""))
                //版本号
                .version("1.0")
                //描述
                .description("API描述")
                .build();

    }

}
