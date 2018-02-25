package com.ximalaya.glue;

import com.google.common.collect.*;
import com.ximalaya.glue.utils.MethodUtils;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentMap;


public class GlueContext {
    private final static TreeMultimap<Key, Value> interfaceAndObjectMap = TreeMultimap.create();

    private final static ConcurrentMap<String, Integer> nameAndOrderMap = Maps.newConcurrentMap();

    private final static Multimap<String, GlueHandler> nameAndAfterGlueHandlerMap = ArrayListMultimap.create();

    private final static Multimap<String, GlueHandler> nameAndBeforeGlueHandlerMap = ArrayListMultimap.create();

    public static void putAfter(Class listenerIfaceClass, String methodName, String name, String after, Object instance) {
        Integer afterOrder = nameAndOrderMap.get(after);
        if (null == afterOrder) {
            nameAndAfterGlueHandlerMap.put(after, new GlueHandler(listenerIfaceClass, methodName, name, instance));
            return;
        }
        Integer order = afterOrder + 1;
        put(listenerIfaceClass, methodName, name, order, instance);
    }

    public static void putBefore(Class listenerIfaceClass, String methodName, String name, String before, Object instance) {
        Integer beforeOrder = nameAndOrderMap.get(before);
        if (null == beforeOrder) {
            nameAndBeforeGlueHandlerMap.put(before, new GlueHandler(listenerIfaceClass, methodName, name, instance));
            return;
        }
        Integer order = beforeOrder - 1;
        put(listenerIfaceClass, methodName, name, order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, String name, Object instance) {
        put(listenerIfaceClass, methodName, name, GlueConstant.default_order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, int order, Object instance) {
        put(listenerIfaceClass, methodName, Key.key(listenerIfaceClass, methodName), order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, Object instance) {
        put(listenerIfaceClass, methodName, Key.key(listenerIfaceClass, methodName), GlueConstant.default_order, instance);
    }

    public static void put(Class listenerIfaceClass, Object instance) {
        String methodName = MethodUtils.singleMethodName(listenerIfaceClass);
        put(listenerIfaceClass, methodName, Key.key(listenerIfaceClass, methodName), GlueConstant.default_order, instance);
    }


    public static void put(Class listenerIfaceClass, String methodName, String name, int order, Object instance) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name can not be null");
        }
        Key key = new Key(listenerIfaceClass, methodName);
        Value value = new Value(instance, order);
        interfaceAndObjectMap.put(key, value);
        nameAndOrderMap.put(name, order);

        /*
            将原先没有注册上的，注册上
         */
        if (nameAndAfterGlueHandlerMap.containsKey(name)) {
            put(nameAndAfterGlueHandlerMap.get(name), order + 1);
        }
        if (nameAndBeforeGlueHandlerMap.containsKey(name)) {
            put(nameAndBeforeGlueHandlerMap.get(name), order - 1);
        }
    }

    private static void put(Collection<GlueHandler> glueHandlers, int order) {
        for (GlueHandler glueHandler : glueHandlers) {
            put(glueHandler, order);
        }
    }

    private static void put(GlueHandler glueHandler, int order) {
        Key key = new Key(glueHandler.getListenerIfaceClass(), glueHandler.getMethodName());
        Value value = new Value(glueHandler.getInstance(), order);
        interfaceAndObjectMap.put(key, value);
        nameAndOrderMap.put(glueHandler.getName(), order);
    }

    public static List<Object> get(Class listenerIfaceClass, String methodName) {
        Key key = new Key(listenerIfaceClass, methodName);
        SortedSet<Value> valueSet = interfaceAndObjectMap.get(key);
        List<Object> instances = Lists.newArrayList();
        for (Value value : valueSet) {
            instances.add(value.getInstance());
        }
        return instances;
    }

    public static List<Object> get(Class listenerIfaceClass) {
        return get(listenerIfaceClass, MethodUtils.singleMethodName(listenerIfaceClass));
    }

    static class Value implements Comparable<Value> {

        private Object instance;
        private int order;

        public Value(Object instance, int order) {
            this.instance = instance;
            this.order = order;
        }

        public Object getInstance() {
            return instance;
        }

        public int getOrder() {
            return order;
        }

        /*
            按order排序
         */
        public int compareTo(Value o) {
            return this.order - o.getOrder();
        }
    }

    static class Key implements Comparable<Key> {
        private Class clazz;
        private String methodName;

        private String key;

        public Key(Class clazz, String methodName) {
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
        public int compareTo(Key o) {
            return key.compareTo(o.key());
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

}
