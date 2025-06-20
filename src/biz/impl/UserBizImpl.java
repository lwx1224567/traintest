package biz.impl;

import biz.UserBiz;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import entity.User;

public class UserBizImpl implements UserBiz {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }
    @Override
    public boolean register(User user) {
        // 先查重
        if (userDao.findUserByUsername(user.getUsername()) != null) {
            return false; // 用户已存在
        }
        return userDao.addUser(user);
    }
}
