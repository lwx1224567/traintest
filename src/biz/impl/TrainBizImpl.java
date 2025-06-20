package biz.impl;

import biz.TrainBiz;
import dao.TrainDao;
import dao.impl.TrainDaoImpl;
import entity.Train;

import java.util.List;

public class TrainBizImpl implements TrainBiz {

    private TrainDao trainDao = new TrainDaoImpl();

    @Override
    public List<Train> getTodayTrains() {
        System.out.println("【DEBUG-BIZ】调用 getTodayTrains()");
        List<Train> trains = trainDao.getTodayTrains();
        System.out.println("【DEBUG-BIZ】返回车次数量：" + (trains != null ? trains.size() : 0));
        return trains;
    }

    @Override
    public Train getTrainById(int trainId) {
        System.out.println("【DEBUG-BIZ】调用 getTrainById(trainId=" + trainId + ")");
        Train train = trainDao.getTrainById(trainId);
        System.out.println("【DEBUG-BIZ】查询结果：" + train);
        return train;
    }

    @Override
    public boolean addTrain(Train train) {
        System.out.println("【DEBUG-BIZ】调用 addTrain(train=" + train + ")");
        boolean result = trainDao.addTrain(train);
        System.out.println("【DEBUG-BIZ】添加结果：" + result);
        return result;
    }

    @Override
    public boolean updateTrain(Train train) {
        System.out.println("【DEBUG-BIZ】调用 updateTrain(train=" + train + ")");
        boolean result = trainDao.updateTrain(train);
        System.out.println("【DEBUG-BIZ】更新结果：" + result);
        return result;
    }

    @Override
    public boolean deleteTrain(int trainId) {
        System.out.println("【DEBUG-BIZ】调用 deleteTrain(trainId=" + trainId + ")");
        boolean result = trainDao.deleteTrain(trainId);
        System.out.println("【DEBUG-BIZ】删除结果：" + result);
        return result;
    }
}
