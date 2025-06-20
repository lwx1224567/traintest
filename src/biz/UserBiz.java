package biz;

import entity.User;

public interface UserBiz {
    User login(String username, String password);
    boolean register(User user);
}
