package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config.datasource;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;



public class DynamicDataSourceForCustomAOP extends AbstractRoutingDataSource {

    public DynamicDataSourceForCustomAOP(DataSource primaryDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(primaryDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

}
