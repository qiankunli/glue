package com.ximalaya.glue.demo;

import com.ximalaya.glue.Glues;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    public void addUser() {
        User user = new User("lisi");
        //...
        Glues.call(UserChangeListener.class, "onUserAdd", user);
    }
}
