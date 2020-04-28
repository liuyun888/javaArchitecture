package com.demo.ftp;

import com.demo.ftp.common.spring.SpringHelper;
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
public class FtpApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(FtpApplication.class);
    }

    private static final Logger logger = LoggerFactory.getLogger(FtpApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FtpApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        SpringHelper.setApplicationContext(application.run(args));
        FtpApplication.logger.info("FtpApplication start!\t http://localhost:2020/swagger-ui.html");
    }
}
