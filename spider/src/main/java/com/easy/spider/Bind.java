package com.easy.spider;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * project: Phantom
 * Description:绑定接口定义
 * Version: v:1.0
 */
public interface Bind<T> {

    void bind(T object);

    void unbind(T object);
}
