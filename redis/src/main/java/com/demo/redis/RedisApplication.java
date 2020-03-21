package com.demo.redis;

import com.demo.redis.common.spring.SpringHelper;
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
public class RedisApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(RedisApplication.class);
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RedisApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        SpringHelper.setApplicationContext(application.run(args));
        RedisApplication.logger.info("RedisService start!\t http://localhost:1990/swagger-ui.html");


    }



}
