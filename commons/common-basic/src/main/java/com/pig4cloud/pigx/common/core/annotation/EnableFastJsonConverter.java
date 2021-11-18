package com.pig4cloud.pigx.common.core.annotation;

import com.pig4cloud.pigx.common.core.config.FastJsonConvertersAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author
 * @date 2019-09-18
 * <p>
 * 开启支持XXL
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({FastJsonConvertersAutoConfiguration.class})
public @interface EnableFastJsonConverter {
}
