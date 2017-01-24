package com.easy.silken.util;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description: 生成.java类
 * Version: v:1.0
 */
public class ClassBuilderUtil {

    public static void build(Filer filer, String packageName, TypeSpec classTypeSpec) {

        JavaFile javaFile = JavaFile.builder(packageName, classTypeSpec).build();

        try {

            javaFile.writeTo(filer);
        } catch (IOException ioException) {

            ioException.printStackTrace();
        }
    }
}
