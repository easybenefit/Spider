package com.easy.silken.info;

import com.easy.silken.Constants;
import com.squareup.javapoet.TypeName;

/**
 * Created by HandGunBreak on 2016/4/12 - 18:36.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description: @RpcService注解的实例对象
 */

public final class RpcFieldBind {

    public final String name;
    public final TypeName type;

    public RpcFieldBind(String name, TypeName type) {

        this.name = name;
        this.type = type;
    }

    public String getDescription() {

        return "field \'" + this.name + "\'";
    }

    public String getTargetTypeName() {

        return type.toString() + Constants.GRPC_CLASS_SUFFIX;
    }

}

