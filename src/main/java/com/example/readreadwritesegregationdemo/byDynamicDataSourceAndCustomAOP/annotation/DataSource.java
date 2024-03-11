package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.annotation;

import java.lang.annotation.*;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

@Target({ ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceType value() default DataSourceType.PRIMARY;
}
