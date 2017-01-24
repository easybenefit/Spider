package com.easy.silken.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:可变长参数构建
 * Version: v:1.0
 */
@SuppressWarnings("unchecked")
public class VarArgsBuilder<T> {

    private List<T> mArgs;

    public VarArgsBuilder() {

        mArgs = new ArrayList<>();
    }

    public VarArgsBuilder<T> addArg(T className) {

        if (className != null) {

            mArgs.add(className);
        }
        return this;
    }

    public VarArgsBuilder<T> addArgs(Collection<T> classNames) {

        if (classNames != null) {

            mArgs.addAll(classNames);
        }
        return this;
    }

    public Object[] build(Class<T> clazz) {

        if (mArgs != null) {

            T[] args = instance(clazz, mArgs.size());
            if (args != null) {

                return mArgs.toArray(args);
            }
        }
        return null;
    }

    private T[] instance(Class<T> type, int size) {
        try {

            return (T[]) Array.newInstance(type, size);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }


}
