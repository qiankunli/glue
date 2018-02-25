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
        GlueContext.put(UserChangeListener.class, new UserListener());
        UserService userService = new UserService();
        userService.addUser();
    }

    @Test
    public void testTwo() {

        GlueContext.putBefore(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "456", "123",
                new BeforeUserListener());
        GlueContext.putAfter(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "789", "123",
                new AfterUserListener());
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "123",new UserListener());
        UserService userService = new UserService();
        userService.addUser();
    }
}
