package com.ximalaya.glue;

import com.ximalaya.glue.utils.MethodUtils;
import org.junit.Test;

public class UserServiceTest {
    @Test
    public void testEmptyContext() {
        UserService userService = new UserService();
        userService.addUser();
    }

    @Test
    public void test() {
        GlueContext.put(UserChangeListener.class, new UserListener("test"));
        UserService userService = new UserService();
        userService.addUser();
    }

    @Test
    public void testTwo() {

        GlueContext.putBefore(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "456", "123",
                new UserListener("456"));
        GlueContext.putAfter(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "789", "456",
                new UserListener("789"));
        GlueContext.putAfter(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "78910", "123",
                new UserListener("78910"));
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "123",new UserListener("123"));
        GlueContext.putBefore(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "012", "123",
                new UserListener("012"));
        GlueContext.print();
        UserService userService = new UserService();
        userService.addUser();
    }
}
