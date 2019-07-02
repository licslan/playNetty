package com.licslan.dbdao;

import java.util.List;

/**
 * @author LICSLAN
 * 模拟后端获取数据  生产数据  模拟数据库
 * */
public interface MessageDao {

    List<Hobby> getList();
    Hobby getHobby();
}
