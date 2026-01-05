package com.github.salilvnair.ccf.annotation;

import com.github.salilvnair.ccf.config.CcfCoreAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CcfCoreAutoConfiguration.class)
public @interface EnableCcfCore {
}
