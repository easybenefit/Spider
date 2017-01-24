package com.easy.silken.util;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;

import javax.lang.model.element.Modifier;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:方法Builder生成
 * Version: v:1.0
 */
public class MethodBuilderUtil {

    public static MethodSpec.Builder build(String methodName, TypeName returnTypeName, Modifier... modifiers) {

        return MethodSpec.methodBuilder(methodName).addModifiers(modifiers).returns(returnTypeName);
    }

    public static MethodSpec.Builder build(String methodName, Type returnType, Modifier... modifiers) {

        return MethodSpec.methodBuilder(methodName).addModifiers(modifiers).returns(returnType);
    }


    public void test() {


    }


    /**
     * 循环生成字符串 prefix (looper, looper, looper...)
     *
     * @param prefix     前导字符串
     * @param looperSize 次数
     * @param looper     循环串
     * @return result
     */
    public static String buildLooper(String prefix, int looperSize, String looper) {

        if (looperSize <= 0) {

            return prefix;
        }
        StringBuilder builder = new StringBuilder();
        if (StringUtil.isNotEmpty(prefix)) {

            builder.append(prefix);
        }
        boolean first = true;
        for (int i = 0; i < looperSize; i++) {

            if (!first) {

                builder.append(",");
            } else {

                first = false;
            }
            builder.append(looper);
        }
        builder.append(")");
        return builder.toString();
    }

}
