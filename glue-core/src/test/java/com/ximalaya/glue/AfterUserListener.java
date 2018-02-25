package com.ximalaya.glue;


public class AfterUserListener implements UserChangeListener{

    public void onUserAdd(User newUser) {
        System.out.println(this.getClass().getName() + " run...," + newUser.getName());
    }
}
