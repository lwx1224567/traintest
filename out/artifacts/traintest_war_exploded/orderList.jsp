<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的订单列表</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>我的订单列表</h2>

<table border="1" id="orderTable">
    <thead>
    <tr>
        <th>订单ID</th>
        <th>用户ID</th>
        <th>车次编号</th>
        <th>出发地</th>
        <th>目的地</th>
        <th>出发时间</th>
        <th>到达时间</th>
        <th>座位类型</th>
        <th>订单状态</th>
        <th>创建时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody></tbody>
</table>

<script>
    function loadOrders() {
        $.getJSON("OrderServlet?action=list", function(orders) {
            console.log("收到的订单数据:", orders); // 确认数据结构

            let tbody = "";
            orders.forEach(function(order) {
                tbody += `
<tr>
    <td>${"$"}{order.orderId}</td>
    <td>${"$"}{order.userId}</td>
    <td>${"$"}{order.trainNumber}</td>
    <td>${"$"}{order.departure}</td>
    <td>${"$"}{order.destination}</td>
    <td>${"$"}{order.depTime}</td>
    <td>${"$"}{order.arrTime}</td>
    <td>${"$"}{order.seatType}</td>
    <td>${"$"}{order.ordStatus}</td>
    <td>${"$"}{order.creTime}</td>
    <td>
        <button onclick="deleteOrder('${"$"}{order.orderId}')">退票</button>
        <button onclick="updateOrder('${"$"}{order.orderId}', '已改')">改签</button>
    </td>
</tr>
`;
            });

                $("#orderTable tbody").html(tbody);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("加载订单失败:", textStatus, errorThrown);
            alert("加载订单失败: " + textStatus);
        });
    }

    function deleteOrder(id) {
        console.log("deleteOrder调用，orderId=", id);
        if (confirm("确定退票？")) {
            $.getJSON("OrderServlet?action=delete&orderId=" + id, function (res) {
                if (res.success) {
                    loadOrders();
                } else {
                    alert("退票失败: " + (res.error || "未知错误"));
                }
            }).fail(function() {
                alert("退票请求失败");
            });
        }
    }

    function updateOrder(id, status) {
        $.ajax({
            url: "OrderServlet?action=update",
            type: "POST",
            data: {order_id: id, ord_status: status},
            dataType: "json",
            success: function (res) {
                if (res.success) {
                    loadOrders();
                } else {
                    alert("改签失败: " + (res.error || "未知错误"));
                }
            },
            error: function () {
                alert("改签请求失败");
            }
        });
    }

    $(function () {
        loadOrders();
    });
</script>
</body>
</html>
