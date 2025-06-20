package servlet;

import biz.UserBiz;
import biz.impl.UserBizImpl;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserBiz userBiz = new UserBizImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("upassword");

        User user = new User();
        user.setUsername(username);
        user.setUpassword(password);

        PrintWriter out = response.getWriter();
        boolean success = userBiz.register(user);

        if (success) {
                response.sendRedirect("index.jsp?msg=" + URLEncoder.encode("注册成功，请登录", "UTF-8"));
        } else {
            out.print("注册失败，用户名可能已存在！");
        }
    }
}

