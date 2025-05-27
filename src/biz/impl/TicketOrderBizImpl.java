package biz.impl;

import biz.TicketOrderBiz;
import dao.TicketOrderDao;
import dao.impl.TicketOrderDaoImpl;
import entity.TicketOrder;

import java.util.List;

public class TicketOrderBizImpl implements TicketOrderBiz {
    private TicketOrderDao orderDao = new TicketOrderDaoImpl();

    @Override
    public List<TicketOrder> searchOrders(String username, String trainNumber, String status) {
        System.out.println("BizImpl.searchOrders 参数: username=" + username + ", trainNumber=" + trainNumber + ", status=" + status);
        List<TicketOrder> list = orderDao.findAll(username, trainNumber, status);
        System.out.println("BizImpl.searchOrders 查询结果: " + list);
        for (TicketOrder order : list) {
            System.out.println(order); // 如果 toString 被正确重写就可以直接用
        }
        return list;
    }

    @Override
    public boolean addOrder(TicketOrder order) {
        System.out.println("BizImpl.addOrder 参数: " + order);
        boolean result = orderDao.add(order);
        System.out.println("BizImpl.addOrder 结果: " + result);
        return result;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        System.out.println("BizImpl.deleteOrder 参数: orderId=" + orderId);
        boolean result = orderDao.delete(orderId);
        System.out.println("BizImpl.deleteOrder 结果: " + result);
        return result;
    }

    @Override
    public boolean updateOrderStatus(TicketOrder order) {
        System.out.println("BizImpl.updateOrderStatus 参数: " + order);
        boolean result = orderDao.update(order);
        System.out.println("BizImpl.updateOrderStatus 结果: " + result);
        return result;
    }

}

