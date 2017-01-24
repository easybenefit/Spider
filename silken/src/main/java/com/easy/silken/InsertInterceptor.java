package com.easy.silken;

import com.squareup.javapoet.ClassName;

/**
 * Created by HandGunBreak on 2016/4/8 - 14:56.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description: 添加拦截器接口
 */
public interface InsertInterceptor {

    //类级拦截器
    int CLASS_INTERCEPTOR = 1;

    //方法拦截器
    int METHOD_INTERCEPTOR = 2;

    void insertInterceptor(int type, ClassName interceptorClazzName);
}
