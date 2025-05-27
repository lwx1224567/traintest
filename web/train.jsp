<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.impl.TrainDaoImpl,entity.Train" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>可订火车信息</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js"></script>
</head>
<body>
<h2>今日可订车次</h2>

<table border="1" cellpadding="5">
    <tr>
        <th>车次编号</th>
        <th>出发地</th>
        <th>目的地</th>
        <th>出发时间</th>
        <th>到达时间</th>
        <th>座位类型</th>
        <th>票价</th>
        <th>操作</th>
    </tr>
    <%
        List<Train> trains = new TrainDaoImpl().getTodayTrains();  // 获取今日车次
        for (Train t : trains) {
    %>
    <tr>
        <td><%= t.getTrainNumber() %></td>
        <td><%= t.getDeparture() %></td>
        <td><%= t.getDestination() %></td>
        <td><%= t.getDepTime() %></td>
        <td><%= t.getArrTime() %></td>
        <td><%= t.getSeatType() %></td>
        <td><%= t.getPrice() %></td>
        <td>
            <button onclick="bookTicket(<%= t.getTrainId() %>)">订票</button>
        </td>
    </tr>
    <% } %>
</table>

<p><a href="orderList.jsp">查看我的订单</a></p>

<script>
    function bookTicket(trainId) {
        $.post("OrderServlet?action=add", {train_id: trainId}, function (res) {
            if (res.success) {
                alert("订票成功！");
            } else {
                alert("订票失败：" + (res.error || "未知错误"));
            }
        }, "json").fail(function () {
            alert("服务器异常，订票失败！");
        });
    }
</script>
</body>
</html>
