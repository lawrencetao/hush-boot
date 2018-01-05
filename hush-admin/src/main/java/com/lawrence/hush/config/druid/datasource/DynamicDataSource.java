package com.lawrence.hush.config.druid.datasource;

import com.lawrence.hush.util.LogUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<String> local = new ThreadLocal<>();
    private Map<String, String> dataSourceKeyMap;

    public DynamicDataSource() {

    }
    public DynamicDataSource(Map<String, String> dataSourceKeyMap) {
        this.dataSourceKeyMap = dataSourceKeyMap;
    }

    /**
     * 设定当前数据源key
     */
    public static void setJdbcType(String jdbcType) {
        local.set(jdbcType);
    }

    /**
     * 获取当前数据源key
     *
     * @return String
     */
    public static String getJdbcType() {

        return local.get();
    }

    public static void clear(){
    	local.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DynamicDataSource.getJdbcType();

        LogUtil.info(getClass(), "切换到数据源" + key + ", url: " + dataSourceKeyMap.get(key));

        return key;
    }
}
