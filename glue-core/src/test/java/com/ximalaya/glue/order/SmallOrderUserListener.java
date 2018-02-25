package com.ximalaya.glue.order;

import com.ximalaya.glue.User;
import com.ximalaya.glue.UserChangeListener;

public class SmallOrderUserListener implements UserChangeListener {
    @Override
    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
