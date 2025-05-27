package dao;

import entity.Train;
import java.util.List;

public interface TrainDao {
    List<Train> getTodayTrains();
}
