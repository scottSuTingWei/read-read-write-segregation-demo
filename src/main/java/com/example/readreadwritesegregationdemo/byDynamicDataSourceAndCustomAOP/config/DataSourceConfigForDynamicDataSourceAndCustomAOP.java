package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config.datasource.DynamicDataSourceForCustomAOP;
import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.config.datasource.DynamicDataSourceForTransactional;
import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

/**
 * DataSourceConfigForDynamicDataSourceAndCustomAOP
 */
@Configuration
public class DataSourceConfigForDynamicDataSourceAndCustomAOP {
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
    @Primary
    public DataSource dynamicDataSource(
            @Qualifier("primaryDataSource") final DataSource primaryDataSource,
            @Qualifier("replicaDataSource") final DataSource replicationDataSource
    ) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.PRIMARY, primaryDataSource);
        targetDataSources.put(DataSourceType.REPLICATION, replicationDataSource);
        DynamicDataSourceForTransactional dynamicDataSource = new DynamicDataSourceForCustomAOP(primaryDataSource, targetDataSources);
        return dynamicDataSource;
    }
}