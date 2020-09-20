package com.qzero.bt.common.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private IPackedObjectFactory factory;

    @Test
    public void testCollection() throws Exception{
        List<String> list= Arrays.asList("233","666","999");

        PackedObject packedObject= factory.getPackedObject();
        packedObject.addObject("strings",list);

        ObjectMapper mapper=new ObjectMapper();
        //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        log.debug(mapper.writeValueAsString(packedObject));

        List<String> parsed= (List<String>) packedObject.parseCollectionObject("strings",List.class,String.class);

        Assert.assertEquals(parsed,list);

    }

}

