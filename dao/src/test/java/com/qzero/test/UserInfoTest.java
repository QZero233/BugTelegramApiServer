package com.qzero.test;

import com.qzero.bt.dao.UserInfoDao;
import com.qzero.bt.data.UserInfoEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserInfoTest extends BaseTestApplication {

    @Autowired
    private UserInfoDao userInfoDao;

    private UserInfoEntity alice;

    @Before
    public void initAlice(){
        alice=new UserInfoEntity();
        alice.setUserName("Alice");
        alice.setAccountStatus(UserInfoEntity.STATUS_ONLINE);
        alice.setMotto("I'm an alice");
    }

    @Test
    public void testAddUserInfo(){
        userInfoDao.addUserInfo(alice);
        log.info("Add alice over");
    }

    @Test
    public void testDeleteUserInfo(){
        testAddUserInfo();

        userInfoDao.deleteUserInfo("Alice");
        log.info("Delete alice over");

        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo("Alice");
        Assert.assertNull(userInfoEntity);
    }

    @Test
    public void testUpdateUserInfo(){
        testAddUserInfo();
        alice.setMotto("My new motto");
        alice.setAccountStatus(UserInfoEntity.STATUS_LEAVING);
        userInfoDao.updateUserInfo(alice);
        log.info("Update alice over");
        testGetUserInfo();
    }

    @Test
    public void testGetUserInfo(){
        testAddUserInfo();
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo("Alice");
        log.debug(userInfoEntity);
        Assert.assertEquals(alice,userInfoEntity);
    }

}
