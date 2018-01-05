package com.lawrence.hush.config.druid.datasource;

/**
 * 数据源类型枚举
 */
public enum DataSourceType {

	single(SingleProperties.ENUM_TYPE, "默认数据源: single"),
	multi(MultiProperties.ENUM_TYPE, "其他数据源: multi");

    private String type;
    private String name;

    DataSourceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
