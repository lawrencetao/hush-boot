package com.lawrence.hush.model;

import lombok.Data;

import java.util.Date;

@Data
public class Dependencies {

    private Date day;
    private String parent;
    private String child;
    private Long callCount;
    private Long errorCount;

}