package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AccountModifyService;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import com.qzero.bt.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountModifyController {

    @Autowired
    private AccountModifyService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @RequestMapping("/get_token_list")
    public PackedObject getTokenList(@RequestBody PackedObject parameter){
        TokenEntity userToken=parameter.parseObject(TokenEntity.class);

        List<TokenEntity> tokenEntityList=service.getTokenList(userToken);
        PackedObject result;
        if(tokenEntityList==null){
            result=packedObjectFactory.getReturnValue(false,null);
        }else{
            result=packedObjectFactory.getReturnValue(true,null);
            result.addObject("tokenList",tokenEntityList);
        }

        return result;
    }

    @RequestMapping("/change_password")
    public PackedObject changePassword(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity =parameter.parseObject(AuthorizeInfoEntity.class);

        service.changePassword(tokenEntity, authorizeInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }


    @RequestMapping("/freeze_account")
    public PackedObject freezeAccount(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);

        service.freezeAccount(tokenEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @RequestMapping("/unfreeze_account")
    public PackedObject unfreezeAccount(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);

        service.unfreezeAccount(tokenEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @RequestMapping("/get_user_info")
    public PackedObject getUserInfo(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        UserInfoEntity userInfoEntity=service.getUserInfo(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(userInfoEntity);

        return result;
    }

    @RequestMapping("/update_user_info")
    public PackedObject updateUserInfo(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);

        service.updateUserInfo(tokenEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

}
