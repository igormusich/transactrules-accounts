package com.transactrules.accounts.config;


import com.transactrules.accounts.DatabaseDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {
    @Autowired
    DatabaseDriver databaseDriver;

    @Value("${spring.datasource.driverClassName}")
    public String driverClassName;

    @Value("${spring.datasource.username}")
    public String userName;

    @Value("${spring.datasource.password}")
    public String password;

    @Value("${spring.datasource.jdbcUrl}")
    public String url;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {

        // provision your SQL database instance here

        databaseDriver.generateDataModel();

        return DataSourceBuilder.create().build();
    }
}