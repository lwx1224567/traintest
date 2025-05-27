package servlet;

import biz.UserBiz;
import biz.impl.UserBizImpl;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private UserBiz userBiz = new UserBizImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 确保中文参数正确解析
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String upassword = request.getParameter("upassword");

        // 日志调试
        System.out.println("登录尝试：username=" + username + ", password=" + upassword);

        User user = userBiz.login(username, upassword);

        if (user != null) {
            request.getSession().setAttribute("loginUser", user);
            response.getWriter().write("{\"success\": true}");
        } else {
            response.getWriter().write("{\"success\": false, \"msg\": \"用户名或密码错误\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp); // 允许GET转POST逻辑
    }
}
