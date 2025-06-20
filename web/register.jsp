<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户注册</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .register-box {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 350px;
        }
        .register-box h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .register-box input[type="text"],
        .register-box input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        .register-box input[type="submit"] {
            width: 100%;
            padding: 10px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        .register-box input[type="submit"]:hover {
            background: #218838;
        }
    </style>
</head>
<body>
<div class="register-box">
    <h2>注册账号</h2>
    <form action="RegisterServlet" method="post">
        <input type="text" name="username" placeholder="用户名" required />
        <input type="password" name="upassword" placeholder="密码" required />
        <input type="submit" value="注册" />
    </form>


</div>
</body>
</html>
