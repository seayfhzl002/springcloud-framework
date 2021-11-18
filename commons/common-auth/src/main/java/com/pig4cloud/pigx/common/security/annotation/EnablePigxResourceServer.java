package com.pig4cloud.pigx.common.security.annotation;

import com.pig4cloud.pigx.common.security.component.PigxResourceServerAutoConfiguration;
import com.pig4cloud.pigx.common.security.component.PigxSecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author
 * @date 2018/11/10
 * <p>
 * 资源服务注解
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({PigxResourceServerAutoConfiguration.class, PigxSecurityBeanDefinitionRegistrar.class})
public @interface EnablePigxResourceServer {

}
