package dao;

import entity.TicketOrder;

import java.util.List;

public interface TicketOrderDao {
    List<TicketOrder> findAll(String username, String trainNumber, String status,String seatType);

    boolean add(TicketOrder order);
    boolean delete(int orderId);
    boolean update(int orderId, String seatType, String ordStatus);

}
