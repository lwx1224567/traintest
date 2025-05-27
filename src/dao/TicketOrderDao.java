package dao;

import entity.TicketOrder;

import java.util.List;

public interface TicketOrderDao {
    List<TicketOrder> findAll(String username, String trainNumber, String status);

    boolean add(TicketOrder order);
    boolean delete(int orderId);
    boolean update(TicketOrder order);
}
