<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.impl.TrainDaoImpl, entity.Train" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setCharacterEncoding("UTF-8");
    String date = request.getParameter("date");
    String trainNumber = request.getParameter("trainNumber");
    TrainDaoImpl dao = new TrainDaoImpl();
    List<Train> trains = dao.searchTrains(date, trainNumber);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <title>可订火车信息</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js"></script>
    <style>
        .popup-form {
            display: none;
            position: fixed;
            top: 10%;
            left: 30%;
            width: 40%;
            background: #f0f0f0;
            padding: 20px;
            border: 1px solid #999;
        }
    </style>
</head>
<body>
<h2>今日可订车次</h2>

<!-- 搜索区域 -->
<form method="get" action="train.jsp">
    日期：<input type="date" name="date" value="<%= date != null ? date : "" %>"/>
    车次：<input type="text" name="trainNumber" value="<%= trainNumber != null ? trainNumber : "" %>"/>
    <input type="submit" value="查询" />
</form>

<!-- 添加按钮 -->
<button onclick="showForm()">添加车次</button>

<!-- 表格展示 -->
<table border="1" cellpadding="5">
    <thead>
    <tr>
        <th>车次编号</th><th>出发地</th><th>目的地</th><th>出发时间</th><th>到达时间</th><th>座位类型</th><th>票价</th><th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Train t : trains) {
    %>
    <tr>
        <td><%= t.getTrainNumber() %></td>
        <td><%= t.getDeparture() %></td>
        <td><%= t.getDestination() %></td>
        <td><%= sdf.format(t.getDepTime()) %></td>
        <td><%= sdf.format(t.getArrTime()) %></td>
        <td><%= t.getSeatType() %></td>
        <td><%= t.getPrice() %></td>
        <td>
            <button onclick='bookTicket(<%= t.getTrainId() %>)'>订票</button>
            <button onclick='editTrain(<%= t.getTrainId() %>)'>修改</button>
            <button onclick='deleteTrain(<%= t.getTrainId() %>)'>删除</button>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<!-- 弹出表单：用于添加/编辑 -->
<div class="popup-form" id="trainFormDiv">
    <h3 id="formTitle">添加车次</h3>
    <form id="trainForm">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="trainId" id="trainId">
        车次编号：<input type="text" name="trainNumber" id="trainNumber" required><br><br>
        出发地：<input type="text" name="departure" id="departure" required><br><br>
        目的地：<input type="text" name="destination" id="destination" required><br><br>
        出发时间：<input type="datetime-local" name="depTime" id="depTime" required><br><br>
        到达时间：<input type="datetime-local" name="arrTime" id="arrTime" required><br><br>
        座位类型：<input type="text" name="seatType" id="seatType" required><br><br>
        票价：<input type="number" name="price" step="0.01" id="price" required><br><br>

        <button type="submit">提交</button>
        <button type="button" onclick="hideForm()">取消</button>
    </form>
</div>

<script>
    function showForm(train = null) {
        $("#trainForm")[0].reset();
        if (train) {
            $("#formTitle").text("修改车次");
            $("input[name='action']").val("update");
            $("#trainId").val(train.trainId);
            $("#trainNumber").val(train.trainNumber);
            $("#departure").val(train.departure);
            $("#destination").val(train.destination);
            $("#depTime").val(train.depTime.replace(' ', 'T'));
            $("#arrTime").val(train.arrTime.replace(' ', 'T'));
            $("#seatType").val(train.seatType);
            $("#price").val(train.price);
        } else {
            $("#formTitle").text("添加车次");
            $("input[name='action']").val("add");
        }
        $("#trainFormDiv").show();
    }
    function bookTicket(trainId) {
        $.ajax({
            url: "OrderServlet?action=add",
            method: "POST",
            data: { train_id: trainId },
            success: function (res) {
                if (res.success) {
                    alert("订票成功！");
                } else {
                    alert("订票失败：" + (res.error || ""));
                }
            },
            error: function () {
                alert("订票请求失败！");
            }
        });
    }

    function hideForm() {
        $("#trainFormDiv").hide();
    }

    function editTrain(id) {
        $.get("TrainServlet", { action: "get", id: id }, function (train) {
            showForm(train);
        }, "json");
    }

    function deleteTrain(id) {
        if (confirm("确定要删除吗？")) {
            $.ajax({
                url: "TrainServlet?action=delete",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({ id: id }),
                success: function () {
                    alert("删除成功！");
                    location.reload();
                },
                error: function () {
                    alert("删除失败！");
                }
            });
        }
    }

    $("#trainForm").submit(function (e) {
        e.preventDefault();
        const formData = {
            trainId: $("#trainId").val(),
            trainNumber: $("#trainNumber").val(),
            departure: $("#departure").val(),
            destination: $("#destination").val(),
            depTime: $("#depTime").val(),
            arrTime: $("#arrTime").val(),
            seatType: $("#seatType").val(),
            price: $("#price").val(),
        };
        const action = $("input[name='action']").val();

        $.ajax({
            url: "TrainServlet?action=" + action,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function () {
                alert(action === "add" ? "添加成功" : "更新成功");
                hideForm();
                location.reload();
            },
            error: function () {
                alert("提交失败！");
            }
        });
    });
</script>

</body>
</html>
