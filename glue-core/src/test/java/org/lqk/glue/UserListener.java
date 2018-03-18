package org.lqk.glue;

public class UserListener implements UserChangeListener{

    private String name;

    public UserListener(String name) {
        this.name = name;
    }

    public void onUserAdd(User newUser) {
        System.out.println(name + " run...," + newUser.getName());
    }
}
