<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的订单列表</title>
    <!-- 引入 jQuery 库，方便后续进行 Ajax 请求和 DOM 操作 -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>我的订单列表</h2>
<div>
    <!-- 车次编号输入框 -->
    <label>车次编号: <input type="text" id="trainNumberInput"/></label>
    <!-- 座位类型选择下拉框 -->
    <label>座位类型:
        <select id="seatTypeSelect">
            <option value="">全部</option>
            <option value="硬座">硬座</option>
            <option value="硬卧">硬卧</option>
            <option value="软卧">软卧</option>
            <option value="无座">无座</option>
        </select>
    </label>
    <!-- 点击按钮触发订单列表查询 -->
    <button onclick="loadOrders()">查询</button>
</div>

<!-- 订单列表表格，初始时只有表头 -->
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
    <tbody></tbody> <!-- 表体内容动态填充 -->
</table>

<script>
    // 加载订单列表，根据输入的车次号和座位类型过滤
    function loadOrders() {
        // 获取用户输入的车次号和座位类型
        let trainNumber = $("#trainNumberInput").val();
        let seatType = $("#seatTypeSelect").val();

        // 发送 AJAX GET 请求，调用 OrderServlet 的 list 动作
        $.ajax({
            url: "OrderServlet?action=list",
            method: "GET",
            dataType: "json", // 预期返回 JSON 格式数据
            data: {
                trainNumber: trainNumber,
                seatType: seatType
            },
            success: function (data) {
                let tbody = $("#orderTable tbody");
                tbody.empty(); // 清空表格内容
                // 遍历返回的订单数据数组，构建表格行
                $.each(data, function (index, o) {
                    let row = "<tr>" +
                        "<td>" + o.orderId + "</td>" +
                        "<td>" + o.userId + "</td>" +
                        "<td>" + o.trainNumber + "</td>" +
                        "<td>" + o.departure + "</td>" +
                        "<td>" + o.destination + "</td>" +
                        "<td>" + o.depTime + "</td>" +
                        "<td>" + o.arrTime + "</td>" +
                        "<td>" + o.seatType + "</td>" +
                        "<td>" + o.ordStatus + "</td>" +
                        "<td>" + o.creTime + "</td>" +
                        "<td>" +
                        // “修改”按钮，点击时调用修改座位类型函数
                        "<button onclick=\"modifySeatType('" + o.orderId + "')\">修改</button> " +
                        // “删除”按钮，点击时调用删除订单函数
                        "<button onclick=\"deleteOrder('" + o.orderId + "')\">退票</button>" +
                        "</td>" +
                        "</tr>";
                    tbody.append(row); // 添加行到表格体
                });
            },
            error: function () {
                alert("加载数据失败！");
            }
        });
    }

    // 修改座位类型及订单状态的函数
    function modifySeatType(orderId) {
        // 弹窗输入新的座位类型
        let newSeatType = prompt("请输入新的座位类型（如：硬座、硬卧、软卧、无座）：");
        if (!newSeatType || newSeatType.trim() === "") {
            alert("座位类型不能为空！");
            return;
        }

        // 校验输入是否为有效座位类型之一
        const validTypes = ["硬座", "硬卧", "软卧","无座"];
        if (!validTypes.includes(newSeatType.trim())) {
            alert("座位类型无效！只能是：" + validTypes.join(" / "));
            return;
        }

        // 发送 POST 请求，调用 OrderServlet 的 update 动作，传入订单ID、新座位类型及新状态
        $.ajax({
            url: "OrderServlet?action=update",
            type: "POST",
            dataType: "json",
            data: {
                order_id: orderId,
                seat_type: newSeatType.trim(),
                ord_status: "已改"  // 状态改为“已改”
            },
            success: function (res) {
                if (res.success) {
                    alert("修改成功！");
                    loadOrders(); // 刷新订单列表
                } else {
                    alert("修改失败: " + (res.error || "未知错误"));
                }
            },
            error: function () {
                alert("修改请求失败！");
            }
        });
    }

    // 删除订单（退票）函数
    function deleteOrder(orderId) {
        // 弹窗确认是否删除
        if (confirm("是否确定要退订当前车票？")) {
            // 发送 GET 请求，调用 OrderServlet 的 delete 动作
            $.getJSON("OrderServlet?action=delete&order_id=" + orderId, function (res) {
                if (res.success) {
                    alert("退订成功！");
                    loadOrders(); // 刷新订单列表
                } else {
                    alert("退票失败: " + (res.error || "未知错误"));
                }
            }).fail(function () {
                alert("退票请求失败！");
            });
        }
    }

    // 页面加载完成后自动加载订单列表
    $(function () {
        loadOrders();
    });
</script>
</body>
</html>
