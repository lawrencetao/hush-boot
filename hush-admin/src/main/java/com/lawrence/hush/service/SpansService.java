package com.lawrence.hush.service;

import com.lawrence.hush.model.Spans;

import java.util.List;

public interface SpansService {

    List<Spans> queryByName(String name);
    List<Spans> queryExtraByName(String name);

}
