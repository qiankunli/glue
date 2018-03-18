package org.lqk.glue.demo;


import org.lqk.glue.annotation.GlueListener;
import org.springframework.stereotype.Component;

@Component
public class AfterUserListener implements UserChangeListener{

    @GlueListener(after = "123")
    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
