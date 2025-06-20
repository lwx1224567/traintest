<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.impl.TrainDaoImpl, entity.Train" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setCharacterEncoding("UTF-8");
    String trainIdParam = request.getParameter("trainId");
    Train train = null;
    boolean isEdit = false;
    if (trainIdParam != null && !trainIdParam.trim().isEmpty()) {
        try {
            int trainId = Integer.parseInt(trainIdParam);
            train = new TrainDaoImpl().getTrainById(trainId);
            isEdit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    if (train == null) train = new Train(); // 初始化空对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= isEdit ? "修改车次" : "添加车次" %></title>
</head>
<body>
<h2><%= isEdit ? "修改车次信息" : "添加新车次" %></h2>

<form action="TrainServlet" method="post">
    <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>"/>
    <% if (isEdit) { %>
    <input type="hidden" name="trainId" value="<%= train.getTrainId() %>"/>
    <% } %>

    车次编号：<input type="text" name="trainNumber" value="<%= train.getTrainNumber() != null ? train.getTrainNumber() : "" %>" required/><br><br>
    出发地：<input type="text" name="departure" value="<%= train.getDeparture() != null ? train.getDeparture() : "" %>" required/><br><br>
    目的地：<input type="text" name="destination" value="<%= train.getDestination() != null ? train.getDestination() : "" %>" required/><br><br>
    出发时间：<input type="datetime-local" name="depTime" value="<%= train.getDepTime() != null ? sdf.format(train.getDepTime()) : "" %>" required/><br><br>
    到达时间：<input type="datetime-local" name="arrTime" value="<%= train.getArrTime() != null ? sdf.format(train.getArrTime()) : "" %>" required/><br><br>
    座位类型：<input type="text" name="seatType" value="<%= train.getSeatType() != null ? train.getSeatType() : "" %>" required/><br><br>
    票价：<input type="number" step="0.01" name="price" value="<%= train.getPrice() != null ? train.getPrice() : "" %>" required/><br><br>

    <input type="submit" value="<%= isEdit ? "更新车次" : "添加车次" %>"/>
    <button type="button" onclick="window.location.href='train.jsp'">取消</button>
</form>

</body>
</html>
--%>