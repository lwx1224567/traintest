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

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private TicketOrderBiz orderBiz = new TicketOrderBizImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // 统一入口
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        String action = request.getParameter("action");
        if (action == null) action = "list";
        System.out.println("收到请求: action = " + action);

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

    // 1. 查询订单列表（使用 Gson 输出 JSON）
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("未获取到 session，返回空列表");
            out.write("[]");
            return;
        }

        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            System.out.println("未登录用户，返回空列表");
            out.write("[]");
            return;
        }

        String username = user.getUsername();
        String trainNumber = request.getParameter("trainNumber");
        String status = request.getParameter("status");

        System.out.println("开始查询订单，用户: " + username + ", 车次号: " + trainNumber + ", 状态: " + status);
        List<TicketOrder> list = orderBiz.searchOrders(
                username,
                trainNumber == null ? "" : trainNumber,
                status == null ? "" : status
        );
        System.out.println("共查询到订单数: " + list.size());

        // 使用 Gson 转换为 JSON 字符串
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(list);
        out.print(json);

    }

    // 2. 添加订单（订票）
    private void addOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            System.out.println("未登录，无法添加订单");
            out.write("{\"success\":false, \"error\":\"请先登录再订票\"}");
            return;
        }

        String trainIdStr = request.getParameter("train_id");
        if (trainIdStr == null || trainIdStr.isEmpty()) {
            System.out.println("缺少 train_id 参数");
            out.write("{\"success\":false, \"error\":\"缺少车次ID参数\"}");
            return;
        }

        try {
            int trainId = Integer.parseInt(trainIdStr);
            int userId = user.getUserId();

            TicketOrder order = new TicketOrder();
            order.setUserId(userId);
            order.setTrainId(trainId);
            order.setOrdStatus("已订");

            System.out.println("准备添加订单: userId=" + userId + ", trainId=" + trainId);
            boolean success = orderBiz.addOrder(order);
            System.out.println("添加订单结果: " + success);
            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            System.out.println("车次ID格式错误: " + trainIdStr);
            out.write("{\"success\":false, \"error\":\"车次ID格式错误\"}");
        }
    }

    // 3. 删除订单（退票）
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        String orderIdStr = request.getParameter("order_id");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            System.out.println("缺少 order_id 参数");
            out.write("{\"success\":false, \"error\":\"缺少订单ID参数\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            System.out.println("准备删除订单 ID: " + orderId);
            boolean success = orderBiz.deleteOrder(orderId);
            System.out.println("删除结果: " + success);
            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            System.out.println("订单ID格式错误: " + orderIdStr);
            out.write("{\"success\":false, \"error\":\"订单ID格式错误\"}");
        }
    }

    // 4. 修改订单状态（改签）
    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();

        String orderIdStr = request.getParameter("order_id");
        String newStatus = request.getParameter("ord_status");

        if (orderIdStr == null || newStatus == null || orderIdStr.isEmpty() || newStatus.isEmpty()) {
            System.out.println("缺少参数: order_id=" + orderIdStr + ", ord_status=" + newStatus);
            out.write("{\"success\":false, \"error\":\"缺少参数\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);

            TicketOrder order = new TicketOrder();
            order.setOrderId(orderId);
            order.setOrdStatus(newStatus);

            System.out.println("准备更新订单状态，orderId=" + orderId + ", 新状态=" + newStatus);
            boolean success = orderBiz.updateOrderStatus(order);
            System.out.println("更新状态结果: " + success);
            out.write("{\"success\":" + success + "}");
        } catch (NumberFormatException e) {
            System.out.println("订单ID格式错误: " + orderIdStr);
            out.write("{\"success\":false, \"error\":\"订单ID格式错误\"}");
        }
    }
}
