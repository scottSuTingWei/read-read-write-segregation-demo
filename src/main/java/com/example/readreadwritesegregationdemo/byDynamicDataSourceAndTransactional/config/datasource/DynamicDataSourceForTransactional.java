package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

import javax.sql.DataSource;
import java.util.Map;;

/**
* TransactionのreadOnlyをもとにDataSourceをルーティングするためのカスタムルーティングクラス
*/
public class DynamicDataSourceForTransactional extends AbstractRoutingDataSource {

    public DynamicDataSourceForTransactional(DataSource primaryDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(primaryDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? DataSourceType.REPLICATION
                : DataSourceType.PRIMARY;
    }  
}