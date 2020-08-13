package com.qzero.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qzero.bt.authorize")
public class DaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaoApplication.class,args);
    }

}
