package dao.impl;

import dao.TrainDao;
import entity.Train;
import db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TrainDao 接口的实现类，负责与数据库进行车次信息的增删改查操作。
 */
public class TrainDaoImpl implements TrainDao {

    /**
     * 查询所有车次信息（当前版本无日期筛选）
     */
    @Override
    public List<Train> getTodayTrains() {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT * FROM train"; // 查询所有车次

        System.out.println("【DEBUG】执行 SQL: " + sql);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 遍历结果集，将每条记录封装为 Train 对象
            while (rs.next()) {
                Train t = new Train();
                t.setTrainId(rs.getInt("train_id"));
                t.setTrainNumber(rs.getString("train_number"));
                t.setDeparture(rs.getString("departure"));
                t.setDestination(rs.getString("destination"));
                t.setDepTime(rs.getTimestamp("dep_time"));
                t.setArrTime(rs.getTimestamp("arr_time"));
                t.setSeatType(rs.getString("seat_type"));
                t.setPrice(rs.getBigDecimal("price"));

                System.out.println("【DEBUG】查询结果：train = " + t);
                list.add(t);
            }

        } catch (Exception e) {
            System.out.println("【ERROR】getTodayTrains 出错！");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根据 ID 查询一条车次信息
     */
    @Override
    public Train getTrainById(int trainId) {
        Train train = null;
        String sql = "SELECT * FROM train WHERE train_id = ?";
        System.out.println("【DEBUG】执行 SQL: " + sql + ", 参数 trainId = " + trainId);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trainId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                train = new Train();
                train.setTrainId(rs.getInt("train_id"));
                train.setTrainNumber(rs.getString("train_number"));
                train.setDeparture(rs.getString("departure"));
                train.setDestination(rs.getString("destination"));
                train.setDepTime(rs.getTimestamp("dep_time"));
                train.setArrTime(rs.getTimestamp("arr_time"));
                train.setSeatType(rs.getString("seat_type"));
                train.setPrice(rs.getBigDecimal("price"));

                System.out.println("【DEBUG】查询成功，train = " + train);
            } else {
                System.out.println("【DEBUG】未找到 train_id = " + trainId + " 的记录");
            }

        } catch (Exception e) {
            System.out.println("【ERROR】getTrainById 出错！");
            e.printStackTrace();
        }

        return train;
    }

    /**
     * 添加一条车次记录
     */
    @Override
    public boolean addTrain(Train train) {
        String sql = "INSERT INTO train(train_number, departure, destination, dep_time, arr_time, seat_type, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println("【DEBUG】执行 SQL: " + sql);
        System.out.println("【DEBUG】添加车次数据：" + train);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, train.getTrainNumber());
            ps.setString(2, train.getDeparture());
            ps.setString(3, train.getDestination());
            ps.setTimestamp(4, new Timestamp(train.getDepTime().getTime()));
            ps.setTimestamp(5, new Timestamp(train.getArrTime().getTime()));
            ps.setString(6, train.getSeatType());
            ps.setBigDecimal(7, train.getPrice());

            boolean success = ps.executeUpdate() > 0;
            System.out.println("【DEBUG】添加结果：" + success);
            return success;

        } catch (Exception e) {
            System.out.println("【ERROR】addTrain 出错！");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 更新已有的车次信息
     */
    @Override
    public boolean updateTrain(Train train) {
        String sql = "UPDATE train SET train_number=?, departure=?, destination=?, dep_time=?, arr_time=?, seat_type=?, price=? WHERE train_id=?";
        System.out.println("【DEBUG】执行 SQL: " + sql);
        System.out.println("【DEBUG】更新数据：" + train);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, train.getTrainNumber());
            ps.setString(2, train.getDeparture());
            ps.setString(3, train.getDestination());
            ps.setTimestamp(4, new Timestamp(train.getDepTime().getTime()));
            ps.setTimestamp(5, new Timestamp(train.getArrTime().getTime()));
            ps.setString(6, train.getSeatType());
            ps.setBigDecimal(7, train.getPrice());
            ps.setInt(8, train.getTrainId());

            boolean success = ps.executeUpdate() > 0;
            System.out.println("【DEBUG】更新结果：" + success);
            return success;

        } catch (Exception e) {
            System.out.println("【ERROR】updateTrain 出错！");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除车次记录（根据 trainId）
     */
    @Override
    public boolean deleteTrain(int trainId) {
        String sql = "DELETE FROM train WHERE train_id=?";
        System.out.println("【DEBUG】执行 SQL: " + sql + ", trainId = " + trainId);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trainId);
            boolean success = ps.executeUpdate() > 0;
            System.out.println("【DEBUG】删除结果：" + success);
            return success;

        } catch (Exception e) {
            System.out.println("【ERROR】deleteTrain 出错！");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据日期和车次号（模糊）查询车次列表
     */
    @Override
    public List<Train> searchTrains(String date, String trainNumber) {
        List<Train> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM train WHERE 1=1"); // 动态 SQL 拼接

        // 根据是否传入日期和车次号添加条件
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(dep_time) = ?");
        }
        if (trainNumber != null && !trainNumber.isEmpty()) {
            sql.append(" AND train_number LIKE ?");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // 设置参数
            int index = 1;
            if (date != null && !date.isEmpty()) {
                ps.setString(index++, date);
            }
            if (trainNumber != null && !trainNumber.isEmpty()) {
                ps.setString(index++, "%" + trainNumber + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Train t = new Train();
                t.setTrainId(rs.getInt("train_id"));
                t.setTrainNumber(rs.getString("train_number"));
                t.setDeparture(rs.getString("departure"));
                t.setDestination(rs.getString("destination"));
                t.setDepTime(rs.getTimestamp("dep_time"));
                t.setArrTime(rs.getTimestamp("arr_time"));
                t.setSeatType(rs.getString("seat_type"));
                t.setPrice(rs.getBigDecimal("price"));
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("【ERROR】searchTrains 出错！");
            e.printStackTrace();
        }

        return list;
    }

}
