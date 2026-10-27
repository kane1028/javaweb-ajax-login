package com.pzy.store;

import com.pzy.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户数据仓库：用一个 Map 当"数据库"，不需要安装 MySQL 也能跑。
 * <p>
 * 如果以后要换成真数据库，只改这个类的方法体（内部换成 JDBC 查询），
 * Servlet 一行都不用动。
 */
public class UserStore {

    /** 单例：整个项目只 new 一次 */
    private static final UserStore INSTANCE = new UserStore();

    /** key = 用户名，value = 用户对象 */
    private final Map<String, User> users = new ConcurrentHashMap<>();

    private UserStore() {
        // 预置两个测试账号
        users.put("admin", new User("admin", "123456"));
        users.put("student", new User("student", "123456"));
    }

    public static UserStore getInstance() {
        return INSTANCE;
    }

    /** 按用户名查用户，查不到返回 null */
    public User findByUsername(String username) {
        return users.get(username);
    }
}
