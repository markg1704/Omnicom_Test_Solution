package com.markg1704.omnitest;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        //main method is kept clean.  It initialises the Spring context only
        //Execution of the test can be found in the ExecuteTest class; com.markg1704.omnicom.config.ExecuteTest
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(com.markg1704.omnitest.config.OmniTestConfig.class);
        context.close();


    }
}
