package com.ximalaya.glue.demo;

import com.ximalaya.glue.annotation.GlueListener;
import org.springframework.stereotype.Component;

@Component
public class BeforeUserListener implements UserChangeListener{

    @GlueListener(before = "123")
    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
