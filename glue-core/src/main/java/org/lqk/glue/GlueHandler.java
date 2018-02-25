package org.lqk.glue;

/*
    目的： 减少参数数量
 */
public class GlueHandler {
    private Class listenerIfaceClass;
    private String methodName;
    private String name;
    private Object instance;



    public GlueHandler(Class listenerIfaceClass, String methodName, String name, Object instance) {
        this.listenerIfaceClass = listenerIfaceClass;
        this.methodName = methodName;
        this.name = name;
        this.instance = instance;
    }



    public Class getListenerIfaceClass() {
        return listenerIfaceClass;
    }

    public void setListenerIfaceClass(Class listenerIfaceClass) {
        this.listenerIfaceClass = listenerIfaceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
