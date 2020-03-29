package com.demo.springdataredis;

import com.demo.springdataredis.common.spring.SpringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 */
@EnableSwagger2
@SpringBootApplication
public class SpringDataRedisApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(SpringDataRedisApplication.class);
    }

    private static final Logger logger = LoggerFactory.getLogger(SpringDataRedisApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringDataRedisApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        SpringHelper.setApplicationContext(application.run(args));
        SpringDataRedisApplication.logger.info("SpringDataRedisApplication start!\t http://localhost:2991/swagger-ui.html");

    }


}
