package com.pzy.web;

import com.pzy.model.User;
import com.pzy.store.UserStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录 Servlet。
 * <p>
 * 浏览器用 AJAX 把用户名/密码 POST 过来，这里不跳页面，
 * 只返回一小段 JSON，例如：
 * {"success":true,"message":"登录成功，欢迎 admin！"}
 * 页面上的 JS 再根据 success 决定显示成功还是失败提示。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 中文编码（请求 + 响应）
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 2. 取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 3. 一层层校验，任何一步不通过都直接返回失败 JSON
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            response.getWriter().write(json(false, "用户名和密码不能为空"));
            return;
        }

        User user = UserStore.getInstance().findByUsername(username.trim());
        if (user == null) {
            response.getWriter().write(json(false, "用户名不存在"));
            return;
        }
        if (!user.getPassword().equals(password)) {
            response.getWriter().write(json(false, "密码错误"));
            return;
        }

        // 4. 全部通过 → 登录成功
        response.getWriter().write(json(true, "登录成功，欢迎 " + user.getUsername() + "！"));
    }

    /** 用 GET 访问也能得到同样的结果，方便浏览器地址栏测试 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 拼一段 JSON（只有两个字段，所以手写字符串就够了，不用引第三方库）
     */
    private String json(boolean success, String message) {
        return "{\"success\":" + success + ",\"message\":\"" + escape(message) + "\"}";
    }

    /** 把 JSON 里的特殊字符转义，防止引号把 JSON 串破坏掉 */
    private String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
