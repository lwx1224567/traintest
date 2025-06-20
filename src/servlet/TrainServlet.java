package servlet;

import biz.TrainBiz;
import biz.impl.TrainBizImpl;
import entity.Train;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/TrainServlet")
public class TrainServlet extends HttpServlet {
    private TrainBiz trainBiz = new TrainBizImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        System.out.println("【TrainServlet doGet】action = " + action);

        try {
            if ("list".equals(action)) {
                System.out.println("获取所有车次列表");
                response.getWriter().write(gson.toJson(trainBiz.getTodayTrains()));
            } else if ("get".equals(action)) {
                String idStr = request.getParameter("trainId");
                System.out.println("获取单条车次，trainId = " + idStr);
                int trainId = Integer.parseInt(idStr);
                Train train = trainBiz.getTrainById(trainId);
                response.getWriter().write(gson.toJson(train));
            } else {
                response.getWriter().write("{\"error\":\"不支持的操作\"}");
            }
        } catch (Exception e) {
            System.err.println("【TrainServlet doGet 异常】" + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"服务器内部错误\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String action = request.getParameter("action");
        System.out.println("【TrainServlet doPost】action = " + action);
        Map<String, Object> result = new HashMap<>();

        try {
            if ("add".equals(action)) {
                Train train = parseTrainFromRequest(request);
                System.out.println("添加车次：" + train);
                boolean success = trainBiz.addTrain(train);
                result.put("success", success);
                result.put("msg", success ? "添加成功" : "添加失败");

            } else if ("update".equals(action)) {
                Train train = parseTrainFromRequest(request);
                String trainIdStr = request.getParameter("trainId");
                if (trainIdStr == null || trainIdStr.isEmpty()) {
                    throw new IllegalArgumentException("trainId不能为空");
                }
                train.setTrainId(Integer.parseInt(trainIdStr));
                System.out.println("更新车次：" + train);
                boolean success = trainBiz.updateTrain(train);
                result.put("success", success);
                result.put("msg", success ? "更新成功" : "更新失败");

            } else if ("delete".equals(action)) {
                String trainIdStr = request.getParameter("trainId");
                if (trainIdStr == null || trainIdStr.isEmpty()) {
                    throw new IllegalArgumentException("trainId不能为空");
                }
                int trainId = Integer.parseInt(trainIdStr);
                System.out.println("删除车次，trainId = " + trainId);
                boolean success = trainBiz.deleteTrain(trainId);
                result.put("success", success);
                result.put("msg", success ? "删除成功" : "删除失败");

            } else {
                result.put("success", false);
                result.put("msg", "不支持的操作类型");
                System.out.println("不支持的action类型：" + action);
            }
        } catch (Exception e) {
            System.err.println("【TrainServlet doPost 异常】" + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "异常错误：" + e.getMessage());
        }

        response.getWriter().write(gson.toJson(result));
    }

    private Train parseTrainFromRequest(HttpServletRequest request) throws Exception {
        String trainNumber = request.getParameter("trainNumber");
        String departure = request.getParameter("departure");
        String destination = request.getParameter("destination");
        String depTimeStr = request.getParameter("depTime");
        String arrTimeStr = request.getParameter("arrTime");
        String seatType = request.getParameter("seatType");
        String priceStr = request.getParameter("price");

        System.out.println("解析参数：trainNumber=" + trainNumber +
                ", departure=" + departure + ", destination=" + destination +
                ", depTime=" + depTimeStr + ", arrTime=" + arrTimeStr +
                ", seatType=" + seatType + ", price=" + priceStr);

        if (trainNumber == null || trainNumber.isEmpty() ||
                departure == null || departure.isEmpty() ||
                destination == null || destination.isEmpty() ||
                depTimeStr == null || depTimeStr.isEmpty() ||
                arrTimeStr == null || arrTimeStr.isEmpty() ||
                seatType == null || seatType.isEmpty() ||
                priceStr == null || priceStr.isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date depTime = sdf.parse(depTimeStr.replace("T", " "));
        Date arrTime = sdf.parse(arrTimeStr.replace("T", " "));
        BigDecimal price = new BigDecimal(priceStr);

        Train train = new Train();
        train.setTrainNumber(trainNumber);
        train.setDeparture(departure);
        train.setDestination(destination);
        train.setDepTime(depTime);
        train.setArrTime(arrTime);
        train.setSeatType(seatType);
        train.setPrice(price);

        return train;
    }
}
