package com.lawrence.hush.constant;

public class BaseConstant {

    /**
     * 获取class文件路径
     */
    public static final String getClassPath(Class clz) {

        return clz.getClass().getResource("/").getPath();
    }

}
