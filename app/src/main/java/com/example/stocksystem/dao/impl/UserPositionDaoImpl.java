package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.UserPosition;
import com.example.stocksystem.dao.UserPositionDao;

/**
 * author:zc
 * created on:2020/4/21 17:17
 * description:
 */
public class UserPositionDaoImpl implements UserPositionDao {
    @Override
    public UserPosition findOneUerPosition(int userId, int stockId) {
        return null;
    }

    @Override
    public boolean insertUserPosition(UserPosition userPosition) {
        return false;
    }

    @Override
    public boolean deleteUserPosition(int userId, int stockId) {
        return false;
    }
}
