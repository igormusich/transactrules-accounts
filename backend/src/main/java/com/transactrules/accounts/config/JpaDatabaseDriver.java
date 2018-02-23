package com.transactrules.accounts.config;

import com.transactrules.accounts.DatabaseDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class JpaDatabaseDriver implements DatabaseDriver {

    @Autowired
    DataSourceConfig config;

    private Logger logger = LoggerFactory.getLogger(JpaDatabaseDriver.class);

    @Override
    public void generateDataModel() {
        // JDBC driver name and database URL

            if(!config.driverClassName.equalsIgnoreCase("com.microsoft.sqlserver.jdbc.SQLServerDriver")){
                //not SQL Server, ignore generate DB model
                return;
            }

            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName(config.driverClassName);

                //STEP 3: Open a connection
                logger.info ("Connecting to database...");
                conn = DriverManager.getConnection(getMasterDBUrl(), config.userName, config.password);

                //STEP 4: Execute a query
                logger.info("Create database if not exists...");
                stmt = conn.createStatement();

                String sql = "if db_id('accounts') is null CREATE DATABASE accounts";
                stmt.executeUpdate(sql);

            }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
            }finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }//end finally try
            }//end try
    }

    private String getMasterDBUrl(){
        String [] urlParts = config.url.split(";");
        String url = urlParts[0] + ";databaseName=master";

        return url;
    }
}
