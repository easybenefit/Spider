package com.easy.silken.util;

import javax.lang.model.element.VariableElement;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class DeprecatedCode {

    /**
     * 获取参数类型
     *
     * @param variableElement VariableElement
     * @return 类型字符串
     */
    private static String getParamClassType(VariableElement variableElement) {

        if (variableElement != null) {

            String paramFullName = variableElement.asType().toString();

            if (paramFullName != null && paramFullName.indexOf('<') != -1) {

                return paramFullName.substring(0, paramFullName.indexOf('<'));
            }
            return paramFullName;
        }
        return null;
    }

    /**
     * 获取参数类泛类型
     *
     * @param variableElement VariableElement
     * @return 类泛类型字符串
     */
    private static String getParamClassGenericType(VariableElement variableElement) {

        if (variableElement != null) {

            String paramFullName = variableElement.asType().toString();

            if (paramFullName != null && paramFullName.indexOf('<') != -1 && paramFullName.lastIndexOf('>') != -1) {

                if (paramFullName.lastIndexOf('>') > paramFullName.indexOf('<')) {

                    return paramFullName.substring(paramFullName.indexOf('<') + 1, paramFullName.lastIndexOf('>'));
                }
            }
            return paramFullName;
        }
        return null;
    }
}
