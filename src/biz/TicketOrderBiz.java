package biz;

import entity.TicketOrder;

import java.util.List;

public interface TicketOrderBiz {
    List<TicketOrder> searchOrders(String username, String trainNumber, String status);


    boolean addOrder(TicketOrder order);
    boolean deleteOrder(int orderId);
    boolean updateOrderStatus(TicketOrder order);
}
