package com.ximalaya.glue.order;

import com.ximalaya.glue.*;
import com.ximalaya.glue.utils.MethodUtils;
import org.junit.Test;

public class OrderUserServiceTest {


    @Test
    public void testTwo() {

        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "456", -1,
                new SmallOrderUserListener());
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "789", 1,
                new BigOrderUserListener());
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), new UserListener("test"));
        UserService userService = new UserService();
        userService.addUser();
    }
}
