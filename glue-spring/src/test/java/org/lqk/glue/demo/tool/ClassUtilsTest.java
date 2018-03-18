package org.lqk.glue.demo.tool;

import org.junit.Test;
import org.springframework.util.ClassUtils;

public class ClassUtilsTest {
    @Test
    public void test() {
        Class[] classes = ClassUtils.getAllInterfaces(new CA());
        for (Class clazz : classes) {
            System.out.println(clazz.getSimpleName());
        }
    }

    @Test
    public void test2(){
        System.out.println(ClassUtils.isAssignable(IA.class,CA.class));
    }
}
