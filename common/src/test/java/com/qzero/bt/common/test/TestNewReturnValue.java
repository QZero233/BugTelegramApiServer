package com.qzero.bt.common.test;

import com.qzero.bt.data.AuthorizeInfoEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class TestNewReturnValue {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Test
    public void testParse(){
        AuthorizeInfoEntity authorizeInfoEntity=new AuthorizeInfoEntity();
        authorizeInfoEntity.setUserName("qzero");
        authorizeInfoEntity.setCodeHash("code");
        authorizeInfoEntity.setPasswordHash("password");
        authorizeInfoEntity.setAuthorizeStatus(2);

        NewReturnValue newReturnValue=new NewReturnValue(true,200,"Good",null);
        newReturnValue.addObject("token_",authorizeInfoEntity);
        log.debug(newReturnValue.toString());

        AuthorizeInfoEntity authorizeInfoEntity1=newReturnValue.parseObject("token_",AuthorizeInfoEntity.class);
        Assert.assertEquals(authorizeInfoEntity,authorizeInfoEntity1);
    }

}

