package org.lqk.glue.utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

public class MethodUtils {
    private final static ConcurrentMap<Class, String> classAndSingleMethodNameMap = Maps.newConcurrentMap();

    public static String singleMethodName(Class listenerIfaceClass) {
        String methodName = classAndSingleMethodNameMap.get(listenerIfaceClass);
        if (null != methodName) {
            return methodName;
        }
        Method[] declaredMethods = listenerIfaceClass.getDeclaredMethods();
        if (null == declaredMethods || 0 == declaredMethods.length) {
            throw new IllegalArgumentException("there is no method for " + listenerIfaceClass.getName());
        }
        if (1 < declaredMethods.length) {
            throw new IllegalArgumentException("there is more than one method for " + listenerIfaceClass.getName());
        }
        methodName = declaredMethods[0].getName();
        classAndSingleMethodNameMap.put(listenerIfaceClass, methodName);
        return methodName;
    }
}
