package servlet;

import biz.UserBiz;
import biz.impl.UserBizImpl;
import entity.User;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    // 业务层接口实例，用于处理用户登录业务逻辑
    private UserBiz userBiz = new UserBizImpl();

    // 处理POST请求，即用户提交登录表单时调用
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置请求体编码，避免中文参数乱码
        request.setCharacterEncoding("UTF-8");
        // 设置响应内容类型为JSON，并指定UTF-8编码
        response.setContentType("application/json;charset=UTF-8");

        // 从请求中获取用户名和密码参数
        String username = request.getParameter("username");
        String upassword = request.getParameter("upassword");

        // 输出日志，方便调试登录请求参数
        System.out.println("登录尝试：username=" + username + ", password=" + upassword);

        // 调用业务层登录方法验证用户
        User user = userBiz.login(username, upassword);

        if (user != null) {
            // 登录成功，将用户对象保存到Session，标识用户已登录
            request.getSession().setAttribute("loginUser", user);
            // 返回JSON表示登录成功
            response.getWriter().write("{\"success\": true}");
        } else {
            // 登录失败，返回JSON提示用户名或密码错误
            response.getWriter().write("{\"success\": false, \"msg\": \"用户名或密码错误\"}");
        }
    }

    // 处理GET请求，直接转发到doPost，统一处理登录逻辑
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
