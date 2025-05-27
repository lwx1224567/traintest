package dao.impl;

import dao.UserDao;
import entity.User;
import db.DBUtil;

import java.sql.*;

public class UserDaoImpl implements UserDao {
    public User login(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM User WHERE username=? AND upassword=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setUpassword(rs.getString("upassword"));
                user.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
