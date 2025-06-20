package servlet;

import biz.TicketOrderBiz;
import biz.impl.TicketOrderBizImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.TicketOrder;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 订单处理控制器，负责处理订单的增删改查操作。
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    // 创建订单业务逻辑处理类的实例
    private TicketOrderBiz orderBiz = new TicketOrderBizImpl();

    /**
     * 统一处理 POST 请求，重定向到 doGet 以简化处理逻辑。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * GET 请求统一处理入口，根据传入的 action 参数执行相应操作
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求与响应编码格式
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        String action = request.getParameter("action");
        if (action == null) action = "list"; // 默认操作为查询

        System.out.println("收到请求: action = " + action);

        // 根据 action 参数执行不同逻辑
        switch (action) {
            case "list":
                listOrders(request, response);
                break;
            case "add":
                addOrder(request, response);
                break;
            case "delete":
                deleteOrder(request, response);
                break;
            case "update":
                updateOrder(request, response);
                break;
            default:
                System.out.println("无效操作: " + action);
                response.getWriter().write("{\"error\":\"Invalid action\"}");
        }
    }

    /**
     * 查询订单列表（支持多条件查询）
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false); // 不创建新 session

        // 检查是否已登录
        if (session == null || session.getAttribute("loginUser") == null) {
            System.out.println("用户未登录，返回空订单列表");
            out.write("[]");
            return;
        }

        // 获取当前登录用户信息
        User user = (User) session.getAttribute("loginUser");

        // 获取查询条件参数
        String username = user.getUsername();
        String trainNumber = request.getParameter("trainNumber");
        String seatType = request.getParameter("seatType");

        System.out.println("开始查询订单，用户: " + username +
                ", 车次号: " + trainNumber +
                ", 座位类型: " + seatType);

        // 调用业务层进行查询
        List<TicketOrder> list = orderBiz.searchOrders(
                username,
                trainNumber == null ? "" : trainNumber.trim(),
                "",  // 状态不做筛选
                seatType == null ? "" : seatType.trim()
        );
        System.out.println("共查询到订单数: " + list.size());

        // 使用 Gson 将订单列表转为 JSON 并返回
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(list);
        out.print(json);
    }

    /**
     * 添加订单（订票）
     */
    private void addOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        // 用户未登录
        if (user == null) {
            System.out.println("未登录，无法添加订单");
            out.write("{\"success\":false, \"error\":\"请先登录再订票\"}");
            return;
        }

        // 获取车次 ID
        String trainIdStr = request.getParameter("train_id");

        // 参数校验
        if (trainIdStr == null || trainIdStr.isEmpty()) {
            System.out.println("缺少 train_id 参数");
            out.write("{\"success\":false, \"error\":\"缺少车次ID参数\"}");
            return;
        }

        try {
            int trainId = Integer.parseInt(trainIdStr);
            int userId = user.getUserId();

            // 构造订单对象
            TicketOrder order = new TicketOrder();
            order.setUserId(userId);
            order.setTrainId(trainId);
            order.setOrdStatus("已订");

            System.out.println("准备添加订单: userId=" + userId + ", trainId=" + trainId);

            // 调用业务层添加订单
            boolean success = orderBiz.addOrder(order);
            System.out.println("添加订单结果: " + success);

            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            System.out.println("车次ID格式错误: " + trainIdStr);
            out.write("{\"success\":false, \"error\":\"车次ID格式错误\"}");
        }
    }

    /**
     * 删除订单（退票）
     */
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        String orderIdStr = request.getParameter("order_id");

        // 参数校验
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            System.out.println("缺少 order_id 参数");
            out.write("{\"success\":false, \"error\":\"缺少订单ID参数\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            System.out.println("准备删除订单 ID: " + orderId);

            // 调用业务层删除订单
            boolean success = orderBiz.deleteOrder(orderId);
            System.out.println("删除结果: " + success);

            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            System.out.println("订单ID格式错误: " + orderIdStr);
            out.write("{\"success\":false, \"error\":\"订单ID格式错误\"}");
        }
    }

    /**
     * 修改订单（可修改状态和座位类型）
     */
    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        String orderIdStr = request.getParameter("order_id");
        String newStatus = request.getParameter("ord_status");
        String newSeatType = request.getParameter("seat_type");

        // 校验必要参数
        if (orderIdStr == null || newStatus == null || orderIdStr.isEmpty() || newStatus.isEmpty()) {
            out.write("{\"success\":false, \"error\":\"缺少参数\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);

            // 构造订单对象进行更新
            TicketOrder order = new TicketOrder();
            order.setOrderId(orderId);
            order.setOrdStatus(newStatus);

            if (newSeatType != null && !newSeatType.trim().isEmpty()) {
                order.setSeatType(newSeatType.trim());
            }

            System.out.println("准备更新订单，orderId=" + orderId +
                    ", 新状态=" + newStatus +
                    ", 新座位类型=" + newSeatType);

            // 调用业务层更新订单
            boolean success = orderBiz.updateOrderStatus(order);
            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            out.write("{\"success\":false, \"error\":\"订单ID格式错误\"}");
        }
    }
}
