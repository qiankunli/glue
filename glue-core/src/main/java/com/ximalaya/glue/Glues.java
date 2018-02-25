package com.ximalaya.glue;

import com.ximalaya.glue.utils.MethodUtils;
import org.joor.Reflect;

import java.util.List;

public class Glues {
    public static void call(Class listenerIfaceClass, String methodName, Object... args) {
        List<Object> instances = GlueContext.get(listenerIfaceClass, methodName);
        if (null == instances || instances.isEmpty()) {
            return;
        }
        for (Object instance : instances) {
            Reflect.on(instance).call(methodName, args);
        }
    }

    public static void call(Class listenerIfaceClass, Object... args) {
        List<Object> instances = GlueContext.get(listenerIfaceClass);
        if (null == instances || instances.isEmpty()) {
            return;
        }
        for (Object instance : instances) {
            Reflect.on(instance).call(MethodUtils.singleMethodName(listenerIfaceClass), args);
        }
    }
}
