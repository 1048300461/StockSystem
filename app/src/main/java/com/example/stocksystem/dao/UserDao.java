package com.example.stocksystem.dao;

import com.example.stocksystem.bean.User;

/**
 * author:zc
 * created on:2020/4/21 16:43
 * description:
 */
public interface UserDao {
    /**
     * 插入一条user记录
     * @param user 插入的数据
     * @return 插入的结果
     */
    boolean insertUser(User user);

    /**
     * 删除一条user记录
     * @param userId 删除的userId
     * @return 删除结果
     */
    boolean deleteUser(int userId);

    /**
     * 通过userId查找指定一条记录
     * @param userId 用户id
     * @return 查找结果
     */
    User findOneUser(int userId);
}
