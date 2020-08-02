package com.qzero.bt.authorize;

import com.qzero.bt.authorize.dao.UserInfoDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DaoTest {

    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void testDao(){
        System.out.println(userInfoDao.getUserInfo("qzero")+"");

    }

}
