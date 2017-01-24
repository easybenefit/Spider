package com.easy.silken.util;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class AnnotationElementUtil {

    private static RoundEnvironment mRoundEnvironment;

    public static void setRoundEnvironment(RoundEnvironment roundEnvironment) {

        mRoundEnvironment = roundEnvironment;
    }

    public static Set<? extends Element> getAnnotationElements(Class<? extends Annotation> clazz) {

        return mRoundEnvironment.getElementsAnnotatedWith(clazz);
    }
}
