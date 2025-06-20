package entity;

import java.util.Date;
import java.sql.Timestamp;


 //TicketOrder 类表示一条车票订单记录。
 // 包含用户、车次、订单状态、时间信息以及关联用户和车次的展示字段。

public class TicketOrder {

    // 订单ID，主键
    private int orderId;

    // 用户ID，外键关联 User 表
    private int userId;

    // 火车ID，外键关联 Train 表
    private int trainId;

    // 订单状态，例如 “已支付”、“未支付”、“已取消”等
    private String ordStatus;

    // 创建时间（订单创建时间）
    private Date creTime;

    // 用户名（用于展示，非数据库字段）
    private String username;

    // 车次编号（用于展示，非数据库字段）
    private String trainNumber;

    // 出发地
    private String departure;

    // 目的地
    private String destination;

    // 出发时间
    private Timestamp depTime;

    // 到达时间
    private Timestamp arrTime;

    // 座位类型，例如“一等座”、“二等座”、“硬卧”等
    private String seatType;

    // Getter 和 Setter 方法，封装每个字段的读写操作

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getDepTime() {
        return depTime;
    }

    public void setDepTime(Timestamp depTime) {
        this.depTime = depTime;
    }

    public Timestamp getArrTime() {
        return arrTime;
    }

    public void setArrTime(Timestamp arrTime) {
        this.arrTime = arrTime;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    // 重写 toString() 方法，方便打印对象信息
    @Override
    public String toString() {
        return "TicketOrder{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", trainId=" + trainId +
                ", ordStatus='" + ordStatus + '\'' +
                ", creTime=" + creTime +
                ", username='" + username + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", depTime='" + depTime + '\'' +
                ", arrTime='" + arrTime + '\'' +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}
