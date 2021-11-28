package com.pig4cloud.pigx.common.swagger.annotation;

import com.pig4cloud.pigx.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author
 * @date 2018/7/21
 * 开启pigx swagger
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnablePigxSwagger2 {
}
