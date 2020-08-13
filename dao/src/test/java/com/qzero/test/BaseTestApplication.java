package com.qzero.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DaoApplication.class)
public class BaseTestApplication {

    protected static final Logger log= LogManager.getLogger();

    @Before
    public void before(){
        log.trace("---------------Test started---------------");
    }

    @After
    public void after(){
        log.trace("---------------Test stopped---------------");
    }


}
