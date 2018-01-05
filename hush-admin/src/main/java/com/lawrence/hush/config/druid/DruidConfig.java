package com.lawrence.hush.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.lawrence.hush.util.LogUtil;
import com.lawrence.hush.util.StringUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DruidConfig {

    /**
     * 初始化druid数据源
     *
     * @param dataSource
     */
    public void initDatasource(DruidDataSource dataSource, DruidProperties properties) {
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        String password = properties.getPassword();

        if (StringUtil.isNotNull(password)) {
            dataSource.setPassword(password);
        }

        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setInitialSize(properties.getInitialSize());// 定义初始连接数
        dataSource.setMinIdle(properties.getMinIdle());// 最小空闲
        dataSource.setMaxActive(properties.getMaxActive());// 定义最大连接数
        dataSource.setMaxWait(properties.getMaxWait());// 最长等待时间

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(properties.getValidationQuery());
        dataSource.setTestWhileIdle(properties.getTestWhileIdle());
        dataSource.setTestOnBorrow(properties.getTestOnBorrow());
        dataSource.setTestOnReturn(properties.getTestOnReturn());

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(properties.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            dataSource.setFilters(properties.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LogUtil.info(getClass(), "加载数据源: " + properties.getUrl());

    }

    /**
     * Druid监控登陆配置
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        // 监控登陆ip白名单/黑名单, 黑名单优先级高
        /*servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        servletRegistrationBean.addInitParameter("deny", "192.168.0.100");*/

        // 登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "lawrence");
        servletRegistrationBean.addInitParameter("loginPassword", "lawrence");

        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    /**
     * druid过滤器配置
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        // 过滤url-pattern
        filterRegistrationBean.addUrlPatterns("/*");

        // druid不过滤的资源
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }

}
