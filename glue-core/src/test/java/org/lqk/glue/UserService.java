package org.lqk.glue;

public class UserService {
    public void addUser() {
        User user = new User("lisi");
        Glues.call(UserChangeListener.class,  user);
    }
}
