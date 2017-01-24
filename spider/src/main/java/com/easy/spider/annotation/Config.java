package com.easy.spider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:配置类注解
 * Version: v:1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Config {

    String hostname() default "";

    int port() default -1;
}
