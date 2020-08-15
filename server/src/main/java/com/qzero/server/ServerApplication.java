package com.qzero.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.qzero")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }

}
