package com.ximalaya.glue;

import com.google.common.collect.*;
import com.ximalaya.glue.model.GlueListenerInstanceWithOrder;
import com.ximalaya.glue.model.GlueListenerInterfaceAndMethod;
import com.ximalaya.glue.model.GlueListenerNamePerMethod;
import com.ximalaya.glue.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentMap;


public class GlueContext {
    /*
        维护 接口 + 方法 与 对应实例的有序序列
     */
    private final static TreeMultimap<GlueListenerInterfaceAndMethod, GlueListenerInstanceWithOrder> interfaceAndObjectMap = TreeMultimap.create();


    /*
        确定 每一个 GlueListenerNamePerMethod 的name 和 order
     */
    private final static ConcurrentMap<GlueListenerNamePerMethod, Integer> nameAndOrderMap = Maps.newConcurrentMap();

    private final static Multimap<GlueListenerNamePerMethod, GlueHandler> nameAndAfterGlueHandlerMap = ArrayListMultimap.create();

    private final static Multimap<GlueListenerNamePerMethod, GlueHandler> nameAndBeforeGlueHandlerMap = ArrayListMultimap.create();

    private static volatile boolean ready = false;

    private static Logger log = LoggerFactory.getLogger(GlueContext.class);

    public static void putAfter(Class listenerIfaceClass, String methodName, String name, String after, Object instance) {
        /*
            为什么 key 是 methodName + name
            一个接口有多个方法，若是每个方法的 GlueListener 命名  为同样的名字，则容易引起问题
            interface Listener {
                function1
                function2
            }

            对于 AB两个Listener实例，可能function1 先A后B,function2 先B 后A
         */
        GlueListenerNamePerMethod glueListenerNamePerMethod = new GlueListenerNamePerMethod(after, methodName);
        Integer afterOrder = nameAndOrderMap.get(glueListenerNamePerMethod);
        if (null == afterOrder) {
            /*
                表示   after 的order 得出之后，需计算 name 的order
             */
            nameAndAfterGlueHandlerMap.put(glueListenerNamePerMethod,
                    new GlueHandler(listenerIfaceClass, methodName, name, instance));
            return;
        }
        Integer order = afterOrder + 1;
        put(listenerIfaceClass, methodName, name, order, instance);
    }

    public static void putBefore(Class listenerIfaceClass, String methodName, String name, String before, Object instance) {
        GlueListenerNamePerMethod glueListenerNamePerMethod = new GlueListenerNamePerMethod(before, methodName);
        Integer beforeOrder = nameAndOrderMap.get(glueListenerNamePerMethod);
        if (null == beforeOrder) {
            nameAndBeforeGlueHandlerMap.put(glueListenerNamePerMethod,
                    new GlueHandler(listenerIfaceClass, methodName, name, instance));
            return;
        }
        Integer order = beforeOrder - 1;
        put(listenerIfaceClass, methodName, name, order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, String name, Object instance) {
        put(listenerIfaceClass, methodName, name, GlueConstant.default_order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, int order, Object instance) {
        put(listenerIfaceClass, methodName, GlueListenerInterfaceAndMethod.key(listenerIfaceClass, methodName), order, instance);
    }

    public static void put(Class listenerIfaceClass, String methodName, Object instance) {
        put(listenerIfaceClass, methodName, GlueListenerInterfaceAndMethod.key(listenerIfaceClass, methodName), GlueConstant.default_order, instance);
    }

    public static void put(Class listenerIfaceClass, Object instance) {
        String methodName = MethodUtils.singleMethodName(listenerIfaceClass);
        put(listenerIfaceClass, methodName, GlueListenerInterfaceAndMethod.key(listenerIfaceClass, methodName), GlueConstant.default_order, instance);
    }


    public static void put(Class listenerIfaceClass, String methodName, String name, int order, Object instance) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name can not be null");
        }
        GlueListenerInterfaceAndMethod glueListenerInterfaceAndMethod = new GlueListenerInterfaceAndMethod(listenerIfaceClass, methodName);
        GlueListenerInstanceWithOrder glueListenerInstanceWithOrder = new GlueListenerInstanceWithOrder(instance, order);

        interfaceAndObjectMap.put(glueListenerInterfaceAndMethod, glueListenerInstanceWithOrder);
        GlueListenerNamePerMethod glueListenerNamePerMethod = new GlueListenerNamePerMethod(name, methodName);
        nameAndOrderMap.put(glueListenerNamePerMethod, order);

        makeUpOrder(glueListenerNamePerMethod,  order);

        if (nameAndAfterGlueHandlerMap.size() == 0 && nameAndBeforeGlueHandlerMap.size() == 0) {
            ready = true;
        }
    }

    /*
        补全可能依赖 glueListenerNamePerMethod 的所有 glue handler
     */
    private static void makeUpOrder(GlueListenerNamePerMethod glueListenerNamePerMethod, int order) {


        if (nameAndAfterGlueHandlerMap.containsKey(glueListenerNamePerMethod)) {
            Collection<GlueHandler> glueHandlers = nameAndAfterGlueHandlerMap.get(glueListenerNamePerMethod);
            put(glueHandlers, order + 1);
            for (GlueHandler glueHandler : glueHandlers) {
                makeUpOrder(new GlueListenerNamePerMethod(glueHandler.getName(), glueHandler.getMethodName()), order + 1);
            }
            nameAndAfterGlueHandlerMap.removeAll(glueListenerNamePerMethod);
        }
        if (nameAndBeforeGlueHandlerMap.containsKey(glueListenerNamePerMethod)) {
            Collection<GlueHandler> glueHandlers = nameAndBeforeGlueHandlerMap.get(glueListenerNamePerMethod);
            put(glueHandlers, order - 1);
            for (GlueHandler glueHandler : glueHandlers) {
                makeUpOrder(new GlueListenerNamePerMethod(glueHandler.getName(), glueHandler.getMethodName()), order - 1);
            }
            nameAndBeforeGlueHandlerMap.removeAll(glueListenerNamePerMethod);
        }
    }

    private static void put(Collection<GlueHandler> glueHandlers, int order) {
        for (GlueHandler glueHandler : glueHandlers) {
            put(glueHandler, order);
        }
    }

    public static void assertReady() {
        if (!ready) {
            for (GlueListenerNamePerMethod m : nameAndBeforeGlueHandlerMap.keys()) {
                log.error("can not find name {} in method {}", m.getName(), m.getMethodName());
            }
            for (GlueListenerNamePerMethod m : nameAndAfterGlueHandlerMap.keys()) {
                log.error("can not find name {} in method {}", m.getName(), m.getMethodName());
            }
            throw new IllegalStateException("there is unhandled glue listener name");
        }
    }


    private static void put(GlueHandler glueHandler, int order) {
        GlueListenerInterfaceAndMethod glueListenerInterfaceAndMethod = new GlueListenerInterfaceAndMethod(glueHandler.getListenerIfaceClass(), glueHandler.getMethodName());
        GlueListenerInstanceWithOrder glueListenerInstanceWithOrder = new GlueListenerInstanceWithOrder(glueHandler.getInstance(), order);
        interfaceAndObjectMap.put(glueListenerInterfaceAndMethod, glueListenerInstanceWithOrder);
        nameAndOrderMap.put(new GlueListenerNamePerMethod(glueHandler.getName(), glueHandler.getMethodName()), order);
    }

    public static List<Object> get(Class listenerIfaceClass, String methodName) {
        GlueListenerInterfaceAndMethod glueListenerInterfaceAndMethod = new GlueListenerInterfaceAndMethod(listenerIfaceClass, methodName);
        SortedSet<GlueListenerInstanceWithOrder> glueListenerInstanceWithOrderSet = interfaceAndObjectMap.get(glueListenerInterfaceAndMethod);
        List<Object> instances = Lists.newArrayList();
        for (GlueListenerInstanceWithOrder glueListenerInstanceWithOrder : glueListenerInstanceWithOrderSet) {
            instances.add(glueListenerInstanceWithOrder.getInstance());
        }
        return instances;
    }

    public static List<Object> get(Class listenerIfaceClass) {
        return get(listenerIfaceClass, MethodUtils.singleMethodName(listenerIfaceClass));
    }

    public static void print() {
        Set<GlueListenerInterfaceAndMethod> set = interfaceAndObjectMap.keySet();
        for (GlueListenerInterfaceAndMethod glueListenerInterfaceAndMethod : set) {
            Set<GlueListenerInstanceWithOrder> glueListenerInstanceWithOrder = interfaceAndObjectMap.get(glueListenerInterfaceAndMethod);
            String objectList = "";
            for (GlueListenerInstanceWithOrder v : glueListenerInstanceWithOrder) {
                objectList += v.getInstance().getClass().getSimpleName() + ",";
            }
            log.info(" {} ==> {}, object {}", glueListenerInterfaceAndMethod.getClazz().getSimpleName(), glueListenerInterfaceAndMethod.getMethodName(),
                    objectList);
        }
    }

}
