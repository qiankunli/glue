package org.lqk.glue;

import org.junit.Test;

import java.lang.reflect.Method;

public class MethodTest {
    @Test
    public void test(){
        Method[] declaredMethods = UserChangeListener.class.getDeclaredMethods();
        System.out.println(declaredMethods.length);
    }
    @Test
    public void test2() throws NoSuchMethodException {
        Class clazz = UserListener.class;
        Method method = clazz.getMethod("onUserAdd",User.class);
        System.out.println(method.getDeclaringClass());

        Class interfaces[] = clazz.getInterfaces();
        for(Class iface : interfaces){

        }

    }
}
