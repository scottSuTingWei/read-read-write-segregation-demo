package com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.annotation.DataSource;
import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.config.datasource.DynamicDataSourceContextHolder;
import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndTransactional.enums.DataSourceType;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    private final Pattern READ_PATTERN;
    private final Pattern WRITER_PATTERN; // by default we use writer

    public DataSourceAspect(@Value("${spring.datasource.pattern}") String readPattern,
                            @Value("${spring.datasource.replica.pattern}") String writerPattern) {
        READ_PATTERN = Pattern.compile(getRegex(readPattern));
        WRITER_PATTERN = Pattern.compile(getRegex(writerPattern));
    }

    private String getRegex(String str) {
        return str.replaceAll("\\*", ".*")
                                 .replaceAll(" ", "")
                                 .replaceAll(",", "|");
    }

    @Around("within(@com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.annotation.DataSource *)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        point.getTarget();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource != null) {
            // In order to have higher granularity,
            // I make method level annotation has higher priority than the class level.
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value());
        } else {
            if (READ_PATTERN.matcher(method.getName()).matches()) {
                DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.REPLICATION);
            } else {
                DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.PRIMARY);
            }
        }

        try {
            return point.proceed();
        } finally {
            // clear data source after method's execution.
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}
