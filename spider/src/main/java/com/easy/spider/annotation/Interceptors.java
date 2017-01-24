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
 * Description: 多拦截器注解
 * Version: v:1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Interceptors {

    Interceptor[] value();
}
