package com.ximalaya.glue.demo;


import com.ximalaya.glue.annotation.GlueListener;
import org.springframework.stereotype.Component;

@Component
public class AfterUserListener implements UserChangeListener{

    @GlueListener(after = "123")
    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
