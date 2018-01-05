package com.lawrence.hush.model;

import java.util.Date;

public class Dependencies {
    private Date day;

    private String parent;

    private String child;

    private Long callCount;

    private Long errorCount;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent == null ? null : parent.trim();
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child == null ? null : child.trim();
    }

    public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }
}