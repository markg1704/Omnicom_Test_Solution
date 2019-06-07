package com.markg1704.omnitest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Class OmniTestConfig

Spring @Configuration annotated class.  Instantiates Beans and sets global variables.

Variable static final String OMNITEST_SYSTEM_PATH sets the path for the project

Database connection initialised.  Assumes database is in the project directory.  If the
database cannot be opened, the program halts execution.

 */

@Configuration
@ComponentScan(basePackages = "com.markg1704.omnitest")
public class OmniTestConfig {

    public static final String OMNITEST_SYSTEM_PATH;

    static {
        Path path = Paths.get(".").normalize().toAbsolutePath();
        OMNITEST_SYSTEM_PATH = path.toString() + "/";
    }

    @Bean(destroyMethod = "close")
    public Connection connection() {
        String url = "jdbc:sqlite:" + OMNITEST_SYSTEM_PATH + "omniDatabase.db";
        Connection connection = null;
        System.out.println("Attempting to connect to the database.");
        try {
            connection = DriverManager.getConnection(url);
        }catch (SQLException e) {
            System.out.println("Problem connecting to database. " + OMNITEST_SYSTEM_PATH + "omniDatabase.db");
            System.out.println("Program is exiting.");
            System.exit(1);
        }
        System.out.println("Connection to database opened. " + OMNITEST_SYSTEM_PATH + "omniDatabase.db");
        return connection;
    }

    public static String getOmnitestSystemPath() {
        return OMNITEST_SYSTEM_PATH;
    }
}
