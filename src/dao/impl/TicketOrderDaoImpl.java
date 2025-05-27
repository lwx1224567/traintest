package dao.impl;

import dao.TicketOrderDao;
import entity.TicketOrder;
import db.DBUtil;

import java.sql.*;
import java.util.*;

public class TicketOrderDaoImpl implements TicketOrderDao {

    @Override
    public List<TicketOrder> findAll(String username, String trainNumber, String status) {
        System.out.println("[DAO] findAll 参数: username=" + username + ", trainNumber=" + trainNumber + ", status=" + status);
        List<TicketOrder> list = new ArrayList<>();
        String sql = "SELECT " +
                "o.order_id, o.user_id, o.train_id, o.ord_status, o.cre_time, " +
                "u.username, " +
                "t.train_number, t.departure, t.destination, t.dep_time, t.arr_time, t.seat_type " +
                "FROM TicketOrder o " +
                "JOIN User u ON o.user_id = u.user_id " +
                "JOIN Train t ON o.train_id = t.train_id " +
                "WHERE u.username LIKE ? AND t.train_number LIKE ? AND o.ord_status LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + username + "%");
            ps.setString(2, "%" + trainNumber + "%");
            ps.setString(3, "%" + status + "%");

            System.out.println("[DAO] SQL 查询已准备: " + sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TicketOrder o = new TicketOrder();
                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTrainId(rs.getInt("train_id"));
                o.setOrdStatus(rs.getString("ord_status"));
                o.setCreTime(rs.getTimestamp("cre_time"));
                o.setUsername(rs.getString("username"));
                o.setTrainNumber(rs.getString("train_number"));
                o.setDeparture(rs.getString("departure"));
                o.setDestination(rs.getString("destination"));
                o.setDepTime(rs.getTimestamp("dep_time"));
                o.setArrTime(rs.getTimestamp("arr_time"));
                o.setSeatType(rs.getString("seat_type"));

                list.add(o);
            }

            System.out.println("[DAO] findAll 查询结果数量: " + list.size());
            for (TicketOrder order : list) {
                System.out.println("[DAO] 查询结果: " + order);
            }

        } catch (Exception e) {
            System.out.println("[DAO] findAll 异常: ");
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean add(TicketOrder order) {
        System.out.println("[DAO] add 参数: " + order);
        String sql = "INSERT INTO TicketOrder (user_id, train_id, ord_status) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getTrainId());
            ps.setString(3, order.getOrdStatus());

            int rows = ps.executeUpdate();
            System.out.println("[DAO] add 执行成功，插入行数: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("[DAO] add 异常: ");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int orderId) {
        System.out.println("[DAO] delete 参数: orderId=" + orderId);
        String sql = "DELETE FROM TicketOrder WHERE order_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            System.out.println("[DAO] delete 执行成功，删除行数: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("[DAO] delete 异常: ");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(TicketOrder order) {
        System.out.println("[DAO] update 参数: " + order);
        String sql = "UPDATE TicketOrder SET ord_status=? WHERE order_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getOrdStatus());
            ps.setInt(2, order.getOrderId());

            int rows = ps.executeUpdate();
            System.out.println("[DAO] update 执行成功，更新行数: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("[DAO] update 异常: ");
            e.printStackTrace();
        }
        return false;
    }
}
