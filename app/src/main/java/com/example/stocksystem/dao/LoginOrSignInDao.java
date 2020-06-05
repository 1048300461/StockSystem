package com.example.stocksystem.dao;

import com.example.stocksystem.bean.User;

/**
*   Date:2020-5-12
*   Author:WangXiao
*   description:登录注册
 */

public interface LoginOrSignInDao {

    /**
     * 判断用户名是否存在
     * @param user
     * @return
     */
    public boolean IsUser(User user);

    /**
     * 判断密码是否正确,密码和用户名相同判断登录类型
     * @param user
     * @return User
     * 返回User类型便于判断登录的类型，管理员和普通用户
     */
    public User IsLogin(User user);

    /**
     * 是否注册成功
     * @param user
     * @return
     */
    public boolean IsSignIn(User user);

    /**
     * 获取数据库中用户数
     * @return
     */
    public int countUser();

    int findUserIdByName(String name);


}
