package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config.datasource;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

public class DynamicDataSourceContextHolder {
    /**
     * ThreadLocalを使用して、他のスレッドによって変更されない、
     * 各スレッドが独自のデータソースコンテクストを持つことを保証します。
     */
    private static final ThreadLocal<DataSourceType> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType dataSourceType){
        CONTEXT_HOLDER.set(dataSourceType);
    }

    public static DataSourceType getDataSourceType(){
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceType(){
        CONTEXT_HOLDER.remove();
    }
}
