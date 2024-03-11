package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import org.springframework.transaction.PlatformTransactionManager;


import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.config.datasource.DynamicDataSourceForTransactional;
import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

// @Configuration
public class DataSourceConfigForDynamicDataSourceAndTransactional {
    
    // primaryDataSource Primary用DBエンドポイントのDataSource
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    // replicationDataSource Replication用DBエンドポイントのDataSource
    @Bean(name = "replicaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * SQLを実行するDataSourceを、Transactionによってルーティングするための設定を作成
     * @param primaryDataSource Primary用DBエンドポイントのDataSource
     * @param replicationDataSource Replication用DBエンドポイントのDataSource
     * @return SQLを実行するDataSourceを、Transactionによってルーティングするための設定
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(
            @Qualifier("primaryDataSource") final DataSource primaryDataSource,
            @Qualifier("replicaDataSource") final DataSource replicationDataSource
    ) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.PRIMARY, primaryDataSource);
        targetDataSources.put(DataSourceType.REPLICATION, replicationDataSource);
        DynamicDataSourceForTransactional dynamicDataSource = new DynamicDataSourceForTransactional(primaryDataSource, targetDataSources);
        return dynamicDataSource;
    }

    /**
     * TransactionによってDataSourceをルーティングするDataSourceを作成
     * @param dynamicDataSource SQLを実行するDataSourceを、Transactionによってルーティングするための設定
     * @return TransactionによってDataSourceをルーティングするDataSource
     */
    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new LazyConnectionDataSourceProxy(dynamicDataSource);
    }

    /**
     * トランザクションマネージャを作成
     * @param dataSource TransactionによってDataSourceをルーティングするDataSource
     * @return トランザクションマネージャ
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}