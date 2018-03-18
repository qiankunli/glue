package com.ximalaya.glue.model;

public class GlueListenerInterfaceAndMethod implements Comparable<GlueListenerInterfaceAndMethod> {
    private Class clazz;
    private String methodName;

    private String key;

    public GlueListenerInterfaceAndMethod(Class clazz, String methodName) {
        this.clazz = clazz;
        this.methodName = methodName;
        key = key(clazz, methodName);
    }

    public Class getClazz() {
        return clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public String key() {
        return key;
    }

    public static String key(Class clazz, String methodName) {
        return clazz.getName() + "&" + methodName;
    }

    /*
        此处用于判断两个key 是否相等
     */
    public int compareTo(GlueListenerInterfaceAndMethod o) {
        return key.compareTo(o.key());
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
