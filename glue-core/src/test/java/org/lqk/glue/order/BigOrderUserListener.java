package org.lqk.glue.order;

import org.lqk.glue.User;
import org.lqk.glue.UserChangeListener;

public class BigOrderUserListener implements UserChangeListener {
    @Override
    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
