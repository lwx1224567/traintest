package dao;

import entity.Train;
import java.util.List;

public interface TrainDao {
    List<Train> getTodayTrains();
    List<Train> searchTrains(String date, String trainNumber);
    Train getTrainById(int trainId);
    boolean addTrain(Train train);
    boolean updateTrain(Train train);
    boolean deleteTrain(int trainId);
}
