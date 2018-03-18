package com.ximalaya.glue;

import com.ximalaya.glue.annotation.GlueListener;
import com.ximalaya.glue.model.GlueListenerInterfaceAndMethod;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class GlueBeanPostProcessor implements BeanPostProcessor {


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        registerToGlueContextIfNeed(bean);

        return bean;
    }

    private void registerToGlueContextIfNeed(Object bean) {
        /*
            记录加入了GlueListener注解的方法
         */

        Class clazz = bean.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            GlueListener annotation = AnnotationUtils.findAnnotation(method, GlueListener.class);
            if (annotation == null) {
                continue;
            }
            Class interfaces[] = ClassUtils.getAllInterfaces(bean);
            String methodName = method.getName();
            for (Class iface : interfaces) {

                if (ClassUtils.hasMethod(iface, methodName, method.getParameterTypes())) {
                    String name = StringUtils.isEmpty(annotation.name()) ? GlueListenerInterfaceAndMethod.key(iface, methodName) : annotation.name();
                    if (!StringUtils.isEmpty(annotation.before())) {
                        GlueContext.putBefore(iface, methodName, name, annotation.before(), bean);
                    } else if (!StringUtils.isEmpty(annotation.after())) {
                        GlueContext.putAfter(iface, methodName, name, annotation.after(), bean);
                    } else {
                        GlueContext.put(iface, methodName, name, annotation.order(), bean);
                    }
                }
            }
        }
    }
}
