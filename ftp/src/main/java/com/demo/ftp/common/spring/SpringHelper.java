package com.demo.ftp.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Spring Boot 相关辅助类
 * @author Administrator
 */
public class SpringHelper {

    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * <p>
     * 获取当前请求
     * </p>
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            return null;
        }
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return servletRequest;
    }

    /**
     * <p>
     * 设置 applicationContext
     * </p>
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (null == APPLICATION_CONTEXT) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * <p>
     * 获取 applicationContext
     * </p>
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * <p>
     * 通过class获取Bean
     * </p>
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
