package dao.impl;

import dao.TrainDao;
import entity.Train;
import db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDaoImpl implements TrainDao {

    @Override
    public List<Train> getTodayTrains() {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT * FROM train"; // 不加 WHERE DATE

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
            e.printStackTrace();
        }

        return list;
    }
}
