package com.qzero.test;

import com.qzero.bt.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AuthorizeInfoTest extends BaseTestApplication {

    @Autowired
    private AuthorizeInfoDao authorizeInfoDao;

    private AuthorizeInfoEntity alice;

    @Before
    public void initAlice(){
        alice=new AuthorizeInfoEntity();
        alice.setUserName("Alice");
        alice.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_ALIVE);
        alice.setCodeHash("codeHash");
        alice.setPasswordHash("passwordHash");
        authorizeInfoDao.addAuthorizeInfo(alice);
    }

    @Test
    public void testDelete(){
        authorizeInfoDao.deleteAuthorizeInfo(alice.getUserName());
        log.info("Delete over");
        AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoDao.getAuthorizeInfoByName(alice.getUserName());
        Assert.assertNull(authorizeInfoEntity);
    }

    @Test
    public void testUpdate(){
        alice.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_FREEZING);
        alice.setCodeHash("newCodeHash");
        alice.setPasswordHash("newPasswordHash");
        authorizeInfoDao.updateAuthorizeInfo(alice);
        log.info("Update alice over");
        testGet();
    }

    @Test
    public void testGet(){
        AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoDao.getAuthorizeInfoByName(alice.getUserName());
        log.debug(authorizeInfoEntity);
        Assert.assertEquals(alice,authorizeInfoEntity);
    }

}
