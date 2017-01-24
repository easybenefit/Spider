package com.easy.silken.util;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class ElementUtil {

    private static Elements mElementUtil;

    public static void setElements(Elements elements) {

        mElementUtil = elements;
    }

    /**
     * 获取类名
     *
     * @param type TypeElement对象
     * @return 类名
     */
    public static String getClassName(TypeElement type) {

        String packageName = getPackageName(type);
        return type.getQualifiedName().toString().substring(packageName.length() + 1);
    }

    /**
     * 获取某个类型的包名
     *
     * @param type TypeElement对象
     * @return 包名
     */
    public static String getPackageName(TypeElement type) {

        return mElementUtil.getPackageOf(type).getQualifiedName().toString();
    }

    /**
     * 查询 TypeMirror
     *
     * @param className typeClassName
     * @return TypeMirror
     */
    public static TypeMirror getTypeMirror(String className) {

        return getTypeElement(className).asType();
    }


    /**
     * 查询 TypeElement
     *
     * @param className typeClassName
     * @return TypeMirror
     */
    public static TypeElement getTypeElement(String className) {

        return mElementUtil.getTypeElement(className);
    }


    /**
     * MirroredTypeException 异常转ClassName
     *
     * @param mirroredTypeException mte
     * @return className
     */
    public static ClassName getClassName(MirroredTypeException mirroredTypeException) {

        TypeElement typeElement = getTypeElement(mirroredTypeException.getTypeMirror().toString());
        return ClassName.get(typeElement);
    }

    /**
     * 泛型类型名
     *
     * @param elementType type
     * @return 名称
     */
    public static String doubleGeneric(TypeMirror elementType) {

        String name = elementType.toString();
        int typeParamStart = name.indexOf('<');
        int typeParamEnd = name.lastIndexOf('>');
        if (typeParamStart != -1 && typeParamEnd != -1 && typeParamEnd > typeParamStart) {

            name = name.substring(typeParamStart + 1, typeParamEnd);
        }
        return name;
    }
}
