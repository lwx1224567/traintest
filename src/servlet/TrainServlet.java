package servlet;

import biz.TrainBiz;
import biz.impl.TrainBizImpl;
import entity.Train;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/TrainServlet") // 映射访问路径为 /TrainServlet
public class TrainServlet extends HttpServlet {
    private TrainBiz trainBiz = new TrainBizImpl(); // 业务逻辑层对象
    // 设置 GSON 用于 JSON 转换，设置时间格式匹配 HTML5 datetime-local 格式
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();

    /**
     * 处理 POST 请求：添加、更新、删除操作
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求和响应编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        // 获取 action 参数，若为空则从请求体中解析
        String action = request.getParameter("action");
        if (action == null) action = getActionFromBody(request);

        System.out.println("[TrainServlet] POST 请求 action = " + action); // 调试日志

        try {
            switch (action) {
                case "add":
                    handleAddOrUpdate(request, response, false); // 添加车次
                    break;
                case "update":
                    handleAddOrUpdate(request, response, true); // 更新车次
                    break;
                case "delete":
                    deleteTrain(request, response); // 删除车次
                    break;
                default:
                    // 无效操作
                    response.getWriter().write("{\"error\":\"Invalid action\"}");
            }
        } catch (Exception e) {
            // 异常处理：返回错误信息
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 处理 GET 请求：查询车次信息
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        String action = request.getParameter("action");
        System.out.println("[TrainServlet] GET 请求 action = " + action); // 调试日志

        try {
            if (action == null || action.equals("list")) {
                listTrains(request, response); // 查询车次列表
            } else if (action.equals("get")) {
                getTrainById(request, response); // 根据 ID 查询单个车次
            } else {
                response.getWriter().write("{\"error\":\"Invalid action\"}");
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 从 JSON 请求体中解析出 action 字段（用于 Ajax 请求中未加 URL 参数的情况）
     */
    private String getActionFromBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        System.out.println("[TrainServlet] 请求体 JSON：" + json); // 打印接收到的 JSON

        // 判断 JSON 中包含的操作类型
        if (json.toString().contains("\"action\":\"update\"")) return "update";
        if (json.toString().contains("\"action\":\"add\"")) return "add";
        if (json.toString().contains("\"action\":\"delete\"")) return "delete";
        return null;
    }

    /**
     * 查询车次列表（可选按日期和车次编号过滤）
     */
    private void listTrains(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String date = request.getParameter("date");
        String trainNumber = request.getParameter("trainNumber");

        System.out.println("[TrainServlet] 查询车次列表，date=" + date + ", trainNumber=" + trainNumber);

        // 调用业务层方法查询
        List<Train> list = trainBiz.searchTrains(
                date == null ? "" : date.trim(),
                trainNumber == null ? "" : trainNumber.trim()
        );

        // 转换为 JSON 返回给前端
        String json = gson.toJson(list);
        response.getWriter().print(json);
    }

    /**
     * 根据车次 ID 查询单个车次信息（用于表单预填）
     */
    private void getTrainById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.getWriter().write("{\"error\":\"缺少id参数\"}");
            return;
        }

        int id = Integer.parseInt(idStr);
        System.out.println("[TrainServlet] 获取车次信息，id=" + id);

        Train train = trainBiz.getTrainById(id); // 查询业务层

        String json = gson.toJson(train);
        response.getWriter().write(json);
    }

    /**
     * 处理添加或更新车次的通用方法
     */
    private void handleAddOrUpdate(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        System.out.println("[TrainServlet] 添加/更新车次，收到 JSON：" + json.toString());

        // 将 JSON 转为 Train 实体对象
        Train train = gson.fromJson(json.toString(), Train.class);
        System.out.println("[TrainServlet] Train 反序列化结果：" + train);

        // 调用业务层方法执行添加或更新
        boolean success = isUpdate ? trainBiz.updateTrain(train) : trainBiz.addTrain(train);

        System.out.println("[TrainServlet] 操作是否成功：" + success);
        // 返回结果给前端
        response.getWriter().write("{\"success\":" + success + "}");
    }

    /**
     * 删除车次信息（通过 JSON 请求体传 id）
     */
    private void deleteTrain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        System.out.println("[TrainServlet] 删除车次，收到 JSON：" + json.toString());

        // 解析 JSON 中的 id 字段
        Map<String, Object> map = gson.fromJson(json.toString(), Map.class);
        double idDouble = (Double) map.get("id"); // JSON 默认是 Double 类型
        int trainId = (int) idDouble;

        System.out.println("[TrainServlet] 删除的 trainId = " + trainId);

        // 调用业务层删除方法
        boolean success = trainBiz.deleteTrain(trainId);
        System.out.println("[TrainServlet] 删除结果：" + success);

        // 返回是否成功
        response.getWriter().write("{\"success\":" + success + "}");
    }
}
