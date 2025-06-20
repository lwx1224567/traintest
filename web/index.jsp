<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>登录页面</title>
  <script src="https://cdn.staticfile.org/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
<h2>登录</h2>

<%-- 注册成功的提示信息 --%>
<%
  String msg = request.getParameter("msg");
  if (msg != null && !msg.isEmpty()) {
%>
<div style="color: green; font-weight: bold; margin-bottom: 10px;"><%= msg %></div>
<%
  }
%>

<form id="loginForm">
  用户名：<input type="text" name="username" id="username" required><br>
  密码：<input type="password" name="upassword" id="upassword" required><br>
  <button type="submit">登录</button>
</form>

<p>还没有账号？<a href="register.jsp">去注册</a></p>

<script>
  $(function () {
    $("#loginForm").on("submit", function (e) {
      e.preventDefault();
      $.ajax({
        url: "login",
        method: "POST",
        data: {
          username: $("#username").val(),
          upassword: $("#upassword").val()
        },
        dataType: "json",
        success: function (res) {
          if (res.success) {
            alert("登录成功！");
            window.location.href = "train.jsp"; // 登录成功后跳转页面
          } else {
            alert(res.msg || "登录失败！");
          }
        },
        error: function () {
          alert("登录请求失败！");
        }
      });
    });
  });
</script>
</body>
</html>
