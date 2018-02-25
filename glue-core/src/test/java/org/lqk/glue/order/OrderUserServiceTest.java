package org.lqk.glue.order;

import org.lqk.glue.utils.MethodUtils;
import org.junit.Test;
import org.lqk.glue.GlueContext;
import org.lqk.glue.UserChangeListener;
import org.lqk.glue.UserListener;
import org.lqk.glue.UserService;

public class OrderUserServiceTest {


    @Test
    public void testTwo() {

        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "456", -1,
                new SmallOrderUserListener());
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), "789", 1,
                new BigOrderUserListener());
        GlueContext.put(UserChangeListener.class, MethodUtils.singleMethodName(UserChangeListener.class), new UserListener());
        UserService userService = new UserService();
        userService.addUser();
    }
}
