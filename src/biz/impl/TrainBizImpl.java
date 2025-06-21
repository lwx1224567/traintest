package biz.impl;

import biz.TrainBiz;
import dao.TrainDao;
import dao.impl.TrainDaoImpl;
import entity.Train;

import java.util.List;

/**
 * TrainBizImpl 是业务逻辑层（Biz Layer）的实现类，
 * 主要负责处理与列车信息相关的业务操作，调用 Dao 层实现数据库操作。
 */
public class TrainBizImpl implements TrainBiz {

    // 创建 Dao 层对象，用于实际操作数据库
    private TrainDao trainDao = new TrainDaoImpl();

    /**
     * 获取今日所有车次（当前实际是返回所有车次）
     */
    @Override
    public List<Train> getTodayTrains() {
        System.out.println("【DEBUG-BIZ】调用 getTodayTrains()");
        List<Train> trains = trainDao.getTodayTrains(); // 调用 Dao 查询
        System.out.println("【DEBUG-BIZ】返回车次数量：" + (trains != null ? trains.size() : 0));
        return trains;
    }

    /**
     * 根据 trainId 获取单个车次信息
     */
    @Override
    public Train getTrainById(int trainId) {
        System.out.println("【DEBUG-BIZ】调用 getTrainById(trainId=" + trainId + ")");
        Train train = trainDao.getTrainById(trainId); // 调用 Dao 查询
        System.out.println("【DEBUG-BIZ】查询结果：" + train);
        return train;
    }

    /**
     * 添加新车次
     */
    @Override
    public boolean addTrain(Train train) {
        System.out.println("【DEBUG-BIZ】调用 addTrain(train=" + train + ")");
        boolean result = trainDao.addTrain(train); // 调用 Dao 插入操作
        System.out.println("【DEBUG-BIZ】添加结果：" + result);
        return result;
    }

    /**
     * 修改已有车次信息
     */
    @Override
    public boolean updateTrain(Train train) {
        System.out.println("【DEBUG-BIZ】调用 updateTrain(train=" + train + ")");
        boolean result = trainDao.updateTrain(train); // 调用 Dao 更新操作
        System.out.println("【DEBUG-BIZ】更新结果：" + result);
        return result;
    }

    /**
     * 删除指定车次
     */
    @Override
    public boolean deleteTrain(int trainId) {
        System.out.println("【DEBUG-BIZ】调用 deleteTrain(trainId=" + trainId + ")");
        boolean result = trainDao.deleteTrain(trainId); // 调用 Dao 删除操作
        System.out.println("【DEBUG-BIZ】删除结果：" + result);
        return result;
    }

    /**
     * 根据日期和车次号搜索车次信息（模糊查询）
     */
    @Override
    public List<Train> searchTrains(String date, String trainNumber) {
        // 这是唯一一个没有打日志的方法，也可以加上：
        System.out.println("【DEBUG-BIZ】调用 searchTrains(date=" + date + ", trainNumber=" + trainNumber + ")");
        List<Train> trains = trainDao.searchTrains(date, trainNumber);
        System.out.println("【DEBUG-BIZ】查询返回车次数量：" + (trains != null ? trains.size() : 0));
        return trains;
    }
}
