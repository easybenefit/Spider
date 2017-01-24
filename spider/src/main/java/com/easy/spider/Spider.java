package com.easy.spider;

import com.easy.spider.callback.ExceptionHandler;
import com.easy.spider.callback.ServiceCallbackAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright: 杭州医本健康科技有限公司(2017)
 * Created by handgunbreak on 17/1/13.
 * Mail: handgunbreak@gmail.com
 * <p>
 * hippo 河马
 * poppy 罂粟花
 * project: Phantom
 * Description:绑定接口定义
 * Version: v:1.0
 */
@SuppressWarnings("unchecked")
public class Spider {

    //打印模式设置
    private static boolean debug = false;
    //Bind生成类后缀
    public static final String BINDING_CLASS_SUFFIX = "_Spider";

    private static final Bind<Object> NOP_SPIDER_BINDER = new Bind<Object>() {

        public void bind(Object var1) {

        }

        public void unbind(Object var1) {

        }
    };

    public static void setDebug(boolean debug) {

        Spider.debug = debug;
    }

    /**
     * 设置全局异常处理器
     * @param exceptionHandler
     */
    public static void setExceptionHandler(ExceptionHandler exceptionHandler) {

        ServiceCallbackAdapter.setExceptionHandler(exceptionHandler);
    }

    private static final Map<Class<?>, Bind<Object>> BINDERS = new LinkedHashMap<>();

    /**
     * 查询绑定对象，即查找由Spider.bind(this)所生成的新类
     *
     * @param clazz 类名
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Bind<Object> findSpiderBinderForClass(Class<?> clazz) throws IllegalAccessException, InstantiationException {

        Bind<Object> thunderBinder = BINDERS.get(clazz);
        if (thunderBinder != null) {

            if (debug) {

                System.out.println("SpiderBinder: " + "HIT: Cached in view binder map.");
            }

            return thunderBinder;
        } else {

            String clazzName = clazz.getName();
            if (!clazzName.startsWith("android.") && !clazzName.startsWith("java.")) {

                try {

                    Class bindingClazz = Class.forName(clazzName + BINDING_CLASS_SUFFIX);
                    thunderBinder = (Bind) bindingClazz.newInstance();
                    if (debug) {

                        System.out.println("SpiderBinder: " + "HIT: Loaded view binder class.");
                    }
                } catch (ClassNotFoundException var4) {

                    if (debug) {

                        System.out.println("SpiderBinder: " + "Not found. Trying superclass " + clazz.getSuperclass().getName());
                    }

                    thunderBinder = findSpiderBinderForClass(clazz.getSuperclass());
                }

                BINDERS.put(clazz, thunderBinder);
                return thunderBinder;
            } else {

                if (debug) {

                    System.out.println("SpiderBinder: " + "MISS: Reached framework class. Abandoning search.");
                }

                return NOP_SPIDER_BINDER;
            }
        }
    }

    /**
     * bind target object
     *
     * @param object object
     */
    public static void bind(Object object) {

        try {

            Class clazz = object.getClass();
            Bind<Object> binder = findSpiderBinderForClass(clazz);
            binder.bind(object);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * unbind target object
     *
     * @param object object
     */
    public static void unbind(Object object) {

        try {

            Class clazz = object.getClass();
            Bind<Object> binder = findSpiderBinderForClass(clazz);
            binder.unbind(object);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

}
