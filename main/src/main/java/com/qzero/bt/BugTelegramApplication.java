package com.qzero.bt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BugTelegramApplication {

    static Logger log= LoggerFactory.getLogger(BugTelegramApplication.class);

    public static void main(String[] args) {
        log.debug("Started");
        SpringApplication.run(BugTelegramApplication.class,args);
    }

}
