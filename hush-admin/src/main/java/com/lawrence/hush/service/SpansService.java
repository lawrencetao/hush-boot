package com.lawrence.hush.service;

import com.lawrence.hush.model.Spans;

import java.util.List;

public interface SpansService {

    List<Spans> querySingleByName(String name);
    List<Spans> queryMultiByName(String name);

}
