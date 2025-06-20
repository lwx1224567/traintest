package dao.impl;

import dao.UserDao;
import entity.User;
import db.DBUtil;

import java.sql.*;

/**
 * UserDaoImpl 是 UserDao 接口的实现类，
 * 实现用户登录验证功能。
 */
public class UserDaoImpl implements UserDao {

    /**
     * 根据用户名和密码验证用户登录信息。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果验证成功，返回 User 对象；否则返回 null
     */
    @Override
    public User login(String username, String password) {
        User user = null;  // 初始化用户对象为 null，表示未登录成功

        // SQL 查询语句，通过用户名和密码查找用户记录
        String sql = "SELECT * FROM User WHERE username=? AND upassword=?";

        // 使用 try-with-resources 自动管理资源（连接、语句）
        try (
                Connection conn = DBUtil.getConnection();  // 获取数据库连接
                PreparedStatement ps = conn.prepareStatement(sql)  // 预编译 SQL
        ) {
            // 设置查询参数（防止 SQL 注入）
            ps.setString(1, username);
            ps.setString(2, password);

            // 执行查询
            ResultSet rs = ps.executeQuery();

            // 如果查询结果存在，则构造并返回对应的 User 对象
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));         // 设置用户 ID
                user.setUsername(rs.getString("username"));   // 设置用户名
                user.setUpassword(rs.getString("upassword")); // 设置密码
            }

        } catch (SQLException e) {
            // 捕获并打印 SQL 异常
            e.printStackTrace();
        }

        return user;  // 返回用户对象（成功则不为 null）
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setUpassword(rs.getString("upassword"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO User(username, upassword) VALUES(?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getUpassword());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

