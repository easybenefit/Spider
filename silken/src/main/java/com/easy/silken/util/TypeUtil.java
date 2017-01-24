package com.easy.silken.util;

import com.easy.spider.callback.Callback;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class TypeUtil {

    private static Types mTypeUtil;

    private static Class CALLBACK_CLASS = Callback.class;
    private static TypeMirror CALLBACK_TYPE_MIRROR = null;

    public static void setTypes(Types types) {

        mTypeUtil = types;
    }

    /**
     * 判断是否继承了GCallback类
     *
     * @param variableElement var
     * @return result
     */
    public static boolean implementsClass(VariableElement variableElement) {

        if (CALLBACK_TYPE_MIRROR == null) {

            CALLBACK_TYPE_MIRROR = ElementUtil.getTypeMirror(CALLBACK_CLASS.getCanonicalName());
        }
        TypeMirror classMirror = variableElement.asType();

        if (classMirror != null) {

            classMirror = mTypeUtil.erasure(classMirror);
        }
        return classMirror != null && mTypeUtil.isAssignable(classMirror, CALLBACK_TYPE_MIRROR);
    }


}
