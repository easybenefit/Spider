package com.easy.silken.util;

import com.easy.silken.Constants;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class Validator {


    /**
     * 检查是否是接口类型
     *
     * @param typeMirror 待检查的TypeMirror
     * @return result
     */
    public static boolean isInterface(TypeMirror typeMirror) {

        return typeMirror instanceof DeclaredType && ((DeclaredType) typeMirror).asElement().getKind() == INTERFACE;
    }


    /**
     * 检查是否是接口类型
     *
     * @param typeMirror 待检查的TypeMirror
     * @return result
     */
    public static boolean isClass(TypeMirror typeMirror) {

        return typeMirror instanceof DeclaredType && ((DeclaredType) typeMirror).asElement().getKind() == CLASS;
    }


    /**
     * 检查注解对象的EnclosingElement是否符合要求
     *
     * @param annotationClass 注解类
     * @param element         被注解的Element
     * @return result
     */
    public static boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass, Element element) {

        TypeElement enclosingElement = (TypeElement) element;
        String qualifiedName = enclosingElement.getQualifiedName().toString();

        if (qualifiedName.startsWith(Constants.ANDROID_PREFIX)) {

            MessageUtil.error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", annotationClass.getClass().getSimpleName(), qualifiedName);
            return true;
        }
        if (qualifiedName.startsWith(Constants.JAVA_PREFIX)) {

            MessageUtil.error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", annotationClass.getClass().getSimpleName(), qualifiedName);
            return true;
        }
        return false;
    }


    /**
     * 判断注解对象有效与否
     *
     * @param clazz   注解类
     * @param element 注解对象
     * @return 是否有效
     */
    public static boolean isElementAnnotationValid(Class<? extends Annotation> clazz, Element element) {

        if (!Validator.isBindingInWrongPackage(clazz, element)) {

            //检查注解对象类型
            if (!Validator.isInterface(element.asType())) {

                MessageUtil.error(element, "@%s-annotated class incorrectly in Interface. (%s)", clazz.getSimpleName(), element.getSimpleName());
                return false;
            }

            if (!(element instanceof TypeElement)) {

                MessageUtil.warning(element, "@%s-annotated class not TypeElement. (%s)", clazz.getSimpleName(), element.getSimpleName());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 检查注解对象访问权限
     *
     * @param annotationClass 注解类
     * @param element         被注解的元素
     * @return 访问权限结果
     */
    public static boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, Element element) {

        boolean hasError = false;
        //获取(eg: @Get注解在RecoverApi.java中的方法，返回com.annotation.RecoverApi，即返回当前元素的上一层的Element)
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Verify method modifiers. 检查方法名的修饰符
        Set<Modifier> modifiers = element.getModifiers();

        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {

            MessageUtil.error(element, "%s must not be private or static, (%s %s)",
                    annotationClass.getSimpleName(), enclosingElement.getQualifiedName().toString(), element.getSimpleName());
            hasError = true;
        }

        // Verify containing methodType.即只能注解类中的方法或者变量，其它则不可，例如方法内的变量注解
        if (enclosingElement.getKind() != INTERFACE && enclosingElement.getKind() != CLASS) {

            MessageUtil.error(element, "%s must be contained in interface or classes, (%s %s)",
                    annotationClass.getSimpleName(), enclosingElement.getQualifiedName().toString(), element.getSimpleName());
            hasError = true;
        }

        // Verify containing class visibility is not private. 类名不能是private
        modifiers = enclosingElement.getModifiers();
        if (modifiers.contains(PRIVATE)) {

            MessageUtil.error(enclosingElement, "@%s must not be contained in private classes. (%s.%s)",
                    annotationClass.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }

        return hasError;
    }
}
