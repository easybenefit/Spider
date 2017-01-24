package com.easy.silken.util;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/14.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:
 * Version: v:
 */
public class ClassNameUtil {

    public static Builder addClassName(ClassName className) {

        return new Builder(className);
    }

    public static class Builder {

        private List<ClassName> mClassNames;

        public Builder(ClassName className) {

            mClassNames = new ArrayList<>();
            addClassName(className);
        }

        public Builder addClassName(ClassName className) {

            if (className != null) {

                mClassNames.add(className);
            }
            return this;
        }

        public Builder addClassName(Collection<ClassName> classNames) {

            if (classNames != null) {

                mClassNames.addAll(classNames);
            }
            return this;
        }

        public Object[] build() {

            return mClassNames.toArray(new ClassName[]{});
        }


    }


}
