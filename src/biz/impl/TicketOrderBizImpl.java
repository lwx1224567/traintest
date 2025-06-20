package biz.impl;

import biz.TicketOrderBiz; // 业务层接口
import dao.TicketOrderDao; // 订单 DAO 接口
import dao.impl.TicketOrderDaoImpl; // DAO 接口实现类
import entity.TicketOrder; // 实体类

import java.util.List;

public class TicketOrderBizImpl implements TicketOrderBiz {
    // 创建 DAO 实例，用于与数据库交互
    private TicketOrderDao orderDao = new TicketOrderDaoImpl();


     //查询订单（支持按用户名、车次号、订单状态、座位类型多条件查询）

    @Override
    public List<TicketOrder> searchOrders(String username, String trainNumber, String status, String seatType) {
        // 打印调试信息（控制台日志）
        System.out.println("BizImpl.searchOrders 参数: username=" + username + ", trainNumber=" + trainNumber + ", status=" + status + ", seatType=" + seatType);

        // 调用 DAO 查询方法，获取订单列表
        List<TicketOrder> list = orderDao.findAll(username, trainNumber, status, seatType);

        // 打印查询结果总览
        System.out.println("BizImpl.searchOrders 查询结果: " + list);

        // 遍历打印每个订单详情（前提是 TicketOrder 重写了 toString 方法）
        for (TicketOrder order : list) {
            System.out.println(order);
        }

        // 返回结果给控制器（Servlet）
        return list;
    }


     //添加订单

    @Override
    public boolean addOrder(TicketOrder order) {
        // 打印要添加的订单信息
        System.out.println("BizImpl.addOrder 参数: " + order);

        // 调用 DAO 的添加方法
        boolean result = orderDao.add(order);

        // 打印添加是否成功
        System.out.println("BizImpl.addOrder 结果: " + result);

        // 返回添加结果
        return result;
    }


     //删除订单（通过订单 ID）

    @Override
    public boolean deleteOrder(int orderId) {
        // 打印要删除的订单 ID
        System.out.println("BizImpl.deleteOrder 参数: orderId=" + orderId);

        // 调用 DAO 删除方法
        boolean result = orderDao.delete(orderId);

        // 打印删除是否成功
        System.out.println("BizImpl.deleteOrder 结果: " + result);

        // 返回删除结果
        return result;
    }


     //更新订单状态和座位类型

    @Override
    public boolean updateOrderStatus(TicketOrder order) {
        // 打印要更新的订单信息（含 ID、座位类型、状态）
        System.out.println("BizImpl.updateOrderStatus 参数: " + order);

        // 调用 DAO 更新方法（按 ID 更新座位类型和订单状态）
        return orderDao.update(order.getOrderId(), order.getSeatType(), order.getOrdStatus());
    }
}
