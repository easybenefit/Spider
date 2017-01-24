package com.easy.silken;

import com.easy.spider.Spider;

/**
 * Created by HandGunBreak on 2016/5/15 - 11:38.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description:
 */
public class Constants {

    //java类
    public static final String JAVA_PREFIX = "java.";

    //android类
    public static final String ANDROID_PREFIX = "android";

    //GGrpc注解的接口生成类后缀
    public static final String GRPC_CLASS_SUFFIX = "_Grpc";

    //@Configure生成类后缀
    public static final String CONFIGURE_CLASS_SUFFIX = "_Manager";

    //由@GService注解生成的新类后缀
    public static final String GRPC_BIND_CLASS_SUFFIX = Spider.BINDING_CLASS_SUFFIX;

}
