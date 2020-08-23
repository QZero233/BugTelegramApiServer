package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AuthorizeService;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private AuthorizeService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @RequestMapping("/login")
    public PackedObject login(@RequestBody PackedObject parameter) throws ResponsiveException {
        TokenEntity tokenEntityForLogin=parameter.parseObject("tokenPreset",TokenEntity.class);
        AuthorizeInfoEntity authorizeInfoEntityForLogin =parameter.parseObject("loginUserInfo", AuthorizeInfoEntity.class);

        TokenEntity tokenEntity=service.login(authorizeInfoEntityForLogin,tokenEntityForLogin);

        PackedObject result=packedObjectFactory.getPackedObject();

        if(tokenEntity==null){
            ActionResult actionResult=new ActionResult(false,"登录失败，也许是密码错误吧");
            result.addObject(actionResult);
        }else{
            ActionResult actionResult=new ActionResult(true,null);
            result.addObject(actionResult);
            result.addObject(tokenEntity);
        }

        return result;
    }

    @RequestMapping("/get_token_detail")
    public PackedObject getTokenDetail(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);

        tokenEntity=service.getTokenById(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(tokenEntity);

        //TODO check if the token is expired

        return result;
    }

    @RequestMapping("/logout")
    public PackedObject logout(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject("logoutToken",TokenEntity.class);

        service.logout(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        return result;
    }

    @RequestMapping("/get_authorize_status")
    public PackedObject getAuthorizeStatus(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);

        int status=service.getAuthorizeStatus(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        result.addObject("authorizeStatus",status);

        return result;
    }

}
