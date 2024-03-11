package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config.datasource;

import java.util.Map;

import javax.sql.DataSource;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.config.datasource.DynamicDataSourceForTransactional;


public class DynamicDataSourceForCustomAOP extends DynamicDataSourceForTransactional{

    public DynamicDataSourceForCustomAOP(DataSource primaryDataSource, Map<Object, Object> targetDataSources) {
        super(primaryDataSource, targetDataSources);
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

}
