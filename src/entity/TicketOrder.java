package entity;

import java.util.Date;
import java.sql.Timestamp;

public class TicketOrder {
    private int orderId;
    private int userId;
    private int trainId;
    private String ordStatus;
    private Date creTime;
    private String username; // 用于连接显示
    private String trainNumber;

    private String departure;
    private String destination;
    private Timestamp depTime;
    private Timestamp arrTime;
    private String seatType;

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
