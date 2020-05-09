package com.example.stocksystem.dao;

import com.example.stocksystem.bean.UserPosition;

/**
 * author:zc
 * created on:2020/4/21 16:48
 * description: 数据库表user_positions的操作
 */
public interface UserPositionDao {
    /**
     * 通过userId和stockId查找指定的一条记录
     * @param userId 用户id
     * @param stockId 股票id
     * @return 查找结果
     */
    UserPosition findOneUerPosition(int userId, int stockId);

    /**
     * 插入一条UserPosition记录
     * @param userPosition 需要添加的数据
     * @return 插入结果
     */
    boolean insertUserPosition(UserPosition userPosition);

    /**
     * 删除特定UserPosition记录
     * @param userId 用户id
     * @param stockId 股票id
     * @return 删除结果
     */
    boolean deleteUserPosition(int userId, int stockId);
}
