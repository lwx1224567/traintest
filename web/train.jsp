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
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 500px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
            padding: 30px;
            z-index: 9999;
            font-family: Arial, sans-serif;
        }

        .popup-form h3 {
            margin-top: 0;
            text-align: center;
            color: #333;
        }

        .popup-form input[type="text"],
        .popup-form input[type="datetime-local"],
        .popup-form input[type="number"] {
            width: 100%;
            padding: 10px;
            margin: 8px 0 16px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        .popup-form button {
            padding: 10px 16px;
            margin: 8px 4px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }

        .popup-form button[type="submit"] {
            background-color: #4CAF50;
            color: white;
        }

        .popup-form button[type="button"] {
            background-color: #ccc;
            color: black;
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
<p><a href="orderList.jsp">查看我的订单</a></p>
<script>
    function showForm(train = null) {
        $("#trainForm")[0].reset();
        if (train) {
            $("#formTitle").text("修改车次");
            $("input[name='action']").val("update");// 设置为 update 模式
            // 预填表单数据
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
        $("#trainFormDiv").show();// 显示弹出层
    }

    function hideForm() {
        $("#trainFormDiv").hide();
    }

    function editTrain(id) {
        // 向后端请求当前 ID 的车次信息，返回 JSON 对象
        $.get("TrainServlet", { action: "get", id: id }, function (train) {
            showForm(train);// 显示编辑弹出框，并预填数据
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
        e.preventDefault();// 阻止表单默认提交行为

        const action = $("input[name='action']").val();// 获取动作 add/update

        const formData = {
            trainNumber: $("#trainNumber").val(),
            departure: $("#departure").val(),
            destination: $("#destination").val(),
            depTime: $("#depTime").val(),
            arrTime: $("#arrTime").val(),
            seatType: $("#seatType").val(),
            price: $("#price").val()
        };

        if (action === "update") {
            formData.trainId = $("#trainId").val();// update 需要 ID
        }

        $.ajax({
            url: "TrainServlet?action=" + action,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (res) {
                if (res.success) {
                    alert(action === "add" ? "添加成功" : "更新成功");
                    hideForm();
                    location.href = "train.jsp";  // 重新加载页面，确保数据更新
                } else {
                    alert("操作失败！");
                }
            },
            error: function () {
                alert("提交失败！");
            }
        });
    });

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
