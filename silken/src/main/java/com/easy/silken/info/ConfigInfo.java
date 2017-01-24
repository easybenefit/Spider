package com.easy.silken.info;

import com.easy.silken.Constants;
import com.squareup.javapoet.ClassName;

import java.util.List;

/**
 * Created by HandGunBreak on 2015/11/6 - 21:01.
 * Mail: HandGunBreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2014-2015)
 * Description: 类级解析信息
 */
public class ConfigInfo {

    //类名
    public String className;
    //包名
    public String packageName;
    //全局拦截器
    public List<ClassName> interceptorClassName;

    /**
     * 注入hostname及port方法名
     */
    public static final String INJECT_METHOD_NAME = "inject";

    /**
     * 获取Channel实例方法名
     */
    public static final String CHANNEL_GET_METHOD_NAME = "getChannel";

    public static final String CONFIG_SUFFIX = "Cfg";

    //返回带后缀的Rpc接口类名
    public String getClassName() {

        if (className == null || className.length() == 0) {

            return null;
        }
        return className + Constants.CONFIGURE_CLASS_SUFFIX;
    }

}
