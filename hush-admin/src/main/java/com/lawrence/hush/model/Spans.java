package com.lawrence.hush.model;

public class Spans {
    private Long traceIdHigh;

    private Long traceId;

    private Long id;

    private String name;

    private Long parentId;

    private Boolean debug;

    private Long startTs;

    private Long duration;

    public Long getTraceIdHigh() {
        return traceIdHigh;
    }

    public void setTraceIdHigh(Long traceIdHigh) {
        this.traceIdHigh = traceIdHigh;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}