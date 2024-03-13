package com.example.readreadwritesegregationdemo.byDifferentEntityManager.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "replicaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.example.readreadwritesegregationdemo.db.entity")
            .persistenceUnit("primary")
            .build();
    }

    @Bean(name = "replicaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean replicaEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("replicaDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.example.readreadwritesegregationdemo.db.entity")
            .persistenceUnit("replica")
            .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }   

    @Bean(name = "replicaTransactionManager")
    public PlatformTransactionManager readTransactionManager(@Qualifier("replicaEntityManagerFactory") EntityManagerFactory readEntityManagerFactory) {
        return new JpaTransactionManager(readEntityManagerFactory);
    }
}