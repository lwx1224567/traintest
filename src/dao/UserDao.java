package dao;

import entity.User;

public interface UserDao {
    User login(String username, String password);
    User findUserByUsername(String username); // 新增
    boolean addUser(User user);               // 新增
}
