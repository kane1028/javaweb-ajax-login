# AJAX 用户登录 Demo（蒲正艺）
## 功能清单

- 登录表单用 `XMLHttpRequest` 异步提交，**页面不刷新**
- Servlet 返回 JSON：`{"success":true,"message":"登录成功，欢迎 admin！"}`
- 三种失败场景分别给出提示：用户名/密码为空、用户名不存在、密码错误
- 登录成功后隐藏登录框，显示欢迎面板（同一个页面里「局部换内容」）
- 中文全程 UTF-8 无乱码（`web.xml` + Servlet 双重设置编码）



## 项目结构

```
ajax-login/
├── pom.xml                                  Maven 配置（war 包）
└── src/main/
    ├── java/com/pzy/
    │   ├── model/User.java                  用户实体（用户名 + 密码）
    │   ├── store/UserStore.java             用户仓库，用 Map 代替数据库
    │   └── web/LoginServlet.java            登录 Servlet，返回 JSON
    └── webapp/
        ├── login.html                       ★ 登录页面（AJAX 代码在这里）
        ├── index.html                       项目说明页
        ├── css/style.css                    页面样式（本地文件，不引 CDN）
        └── WEB-INF/web.xml                  欢迎页 + 中文编码配置
``

## AJAX 关键代码（login.html）

```javascript
var xhr = new XMLHttpRequest();
xhr.open('POST', 'login', true);                              // 请求地址就是 Servlet 的 @WebServlet("/login")
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
        var data = JSON.parse(xhr.responseText);              // {"success":true,"message":"..."}
        showTip(data.message, data.success);
    }
};
xhr.send('username=' + encodeURIComponent(username)
       + '&password=' + encodeURIComponent(password));
```

## 后端关键代码（LoginServlet.java）

```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // ……校验后写回 JSON
        response.getWriter().write("{\"success\":true,\"message\":\"登录成功\"}");
    }
}
```
## 测试账号

| 用户名 | 密码 |
|---|---|
| admin | 123456 |
| student | 123456 |
