package biz;

import entity.Train;
import java.util.List;

    public interface TrainBiz {
        List<Train> getTodayTrains();                // 查询所有或当日火车信息
        Train getTrainById(int trainId);             // 根据ID查询火车信息
        boolean addTrain(Train train);                // 新增火车信息
        boolean updateTrain(Train train);             // 修改火车信息
        boolean deleteTrain(int trainId);             // 删除火车信息
    }

