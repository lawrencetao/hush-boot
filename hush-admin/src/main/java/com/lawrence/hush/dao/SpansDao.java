package com.lawrence.hush.dao;

import com.lawrence.hush.model.Spans;

import java.util.List;

public interface SpansDao {

    int insert(Spans record);
    int insertSelective(Spans record);

    List<Spans> query(String name);

}