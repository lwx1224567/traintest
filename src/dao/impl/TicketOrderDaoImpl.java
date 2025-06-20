package dao.impl;

import dao.TicketOrderDao;
import entity.TicketOrder;
import db.DBUtil;

import java.sql.*;
import java.util.*;

public class TicketOrderDaoImpl implements TicketOrderDao {


     //多条件查询订单列表：根据用户名、车次编号、订单状态和座位类型模糊匹配查询订单信息

    @Override
    public List<TicketOrder> findAll(String username, String trainNumber, String status, String seatType) {
        List<TicketOrder> list = new ArrayList<>();

        // SQL 语句：关联三张表进行条件查询
        String sql = "SELECT " +
                "o.order_id, o.user_id, o.train_id, o.ord_status, o.cre_time, " +
                "u.username, " +
                "t.train_number, t.departure, t.destination, t.dep_time, t.arr_time, t.seat_type " +
                "FROM TicketOrder o " +
                "JOIN User u ON o.user_id = u.user_id " +
                "JOIN Train t ON o.train_id = t.train_id " +
                "WHERE u.username LIKE ? AND t.train_number LIKE ? AND o.ord_status LIKE ? AND t.seat_type LIKE ?";

        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数（模糊查询）
            ps.setString(1, "%" + username + "%");
            ps.setString(2, "%" + trainNumber + "%");
            ps.setString(3, "%" + status + "%");
            ps.setString(4, "%" + seatType + "%");

            System.out.println("[DAO] SQL 查询已准备: " + sql);
            ResultSet rs = ps.executeQuery(); // 执行查询

            // 结果集处理：逐条映射为 TicketOrder 对象
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

                list.add(o); // 添加到结果列表
            }

            // 打印结果
            System.out.println("[DAO] findAll 查询结果数量: " + list.size());
            for (TicketOrder order : list) {
                System.out.println("[DAO] 查询结果: " + order);
            }

        } catch (Exception e) {
            System.out.println("[DAO] findAll 异常: ");
            e.printStackTrace();
        }

        return list; // 返回订单列表
    }


     //添加订单（仅插入 user_id、train_id 和订单状态，座位类型由 Train 表决定）

    @Override
    public boolean add(TicketOrder order) {
        System.out.println("[DAO] add 参数: " + order);
        String sql = "INSERT INTO TicketOrder (user_id, train_id, ord_status) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getTrainId());
            ps.setString(3, order.getOrdStatus());

            int rows = ps.executeUpdate(); // 执行插入语句
            System.out.println("[DAO] add 执行成功，插入行数: " + rows);
            return rows > 0; // 判断是否插入成功

        } catch (Exception e) {
            System.out.println("[DAO] add 异常: ");
            e.printStackTrace();
        }
        return false;
    }


     //删除订单（根据订单 ID）

    @Override
    public boolean delete(int orderId) {
        System.out.println("[DAO] delete 参数: orderId=" + orderId);
        String sql = "DELETE FROM TicketOrder WHERE order_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId); // 设置订单 ID 参数
            int rows = ps.executeUpdate(); // 执行删除
            System.out.println("[DAO] delete 执行成功，删除行数: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("[DAO] delete 异常: ");
            e.printStackTrace();
        }
        return false;
    }


      //更新订单的座位类型（更新 Train 表）和订单状态（更新 TicketOrder 表）
     //需要事务控制，保证同时成功或失败

    @Override
    public boolean update(int orderId, String seatType, String ordStatus) {
        System.out.println("[DAO] update 参数: orderId=" + orderId + ", seatType=" + seatType + ", ord_status=" + ordStatus);

        // 第一步：更新座位类型（更新 Train 表）
        String sql1 = "UPDATE train SET seat_type = ? WHERE train_id = (SELECT train_id FROM ticketorder WHERE order_id = ?)";

        // 第二步：更新订单状态（更新 TicketOrder 表）
        String sql2 = "UPDATE ticketorder SET ord_status = ? WHERE order_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);  // 开启事务

            try (
                    PreparedStatement ps1 = conn.prepareStatement(sql1);
                    PreparedStatement ps2 = conn.prepareStatement(sql2)
            ) {
                // 设置并执行第一条语句
                ps1.setString(1, seatType);
                ps1.setInt(2, orderId);
                int rows1 = ps1.executeUpdate();

                // 设置并执行第二条语句
                ps2.setString(1, ordStatus);
                ps2.setInt(2, orderId);
                int rows2 = ps2.executeUpdate();

                conn.commit(); // 提交事务
                System.out.println("[DAO] update 执行成功，更新行数: " + (rows1 + rows2));
                return (rows1 > 0 || rows2 > 0);

            } catch (Exception e) {
                conn.rollback(); // 出现异常则回滚
                System.out.println("[DAO] update 执行异常，事务已回滚");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("[DAO] update 数据库连接异常: ");
            e.printStackTrace();
        }
        return false;
    }

}
