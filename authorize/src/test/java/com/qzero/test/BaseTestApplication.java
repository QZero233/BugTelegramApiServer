package com.qzero.test;

import com.qzero.bt.authorize.AuthorizeApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AuthorizeApplication.class)
public class BaseTestApplication {

    protected static final Logger log= LoggerFactory.getLogger(BaseTestApplication.class);

    @Before
    public void before(){
        log.trace("---------------Test started---------------");
    }

    @After
    public void after(){
        log.trace("---------------Test stopped---------------");
    }

}
