package com.lawrence.hush.model;

import lombok.Data;

@Data
public class Spans {

    private Long traceIdHigh;
    private Long traceId;
    private Long id;
    private String name;
    private Long parentId;
    private Boolean debug;
    private Long startTs;
    private Long duration;

}