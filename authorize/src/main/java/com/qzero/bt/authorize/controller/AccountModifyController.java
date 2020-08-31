package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AccountModifyService;
import com.qzero.bt.common.permission.PermissionCheck;
import com.qzero.bt.common.permission.PermissionNameList;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import com.qzero.bt.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountModifyController {

    @Autowired
    private AccountModifyService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @GetMapping("/token_list}")
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public PackedObject getTokenList(@RequestHeader("owner_user_name") String userName){
        List<TokenEntity> tokenEntityList=service.getTokenList(userName);
        PackedObject result;
        if(tokenEntityList==null){
            result=packedObjectFactory.getReturnValue(false,null);
        }else{
            result=packedObjectFactory.getReturnValue(true,null);
            result.addObject("tokenList",tokenEntityList);
        }

        return result;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    @PutMapping("/password")
    public PackedObject changePassword(@RequestHeader("owner_user_name") String userName,@RequestBody PackedObject parameter){
        AuthorizeInfoEntity authorizeInfoEntity =parameter.parseObject(AuthorizeInfoEntity.class);
        service.changePassword(userName, authorizeInfoEntity.getPasswordHash());
        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    @PutMapping("/account_frozen")
    public PackedObject freezeAccount(@RequestHeader("owner_user_name") String userName){
        service.freezeAccount(userName);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    @PutMapping("/account_unfrozen")
    public PackedObject unfreezeAccount(@RequestHeader("owner_user_name") String userName){
        service.unfreezeAccount(userName);
        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_READ_USER_INFO)
        @GetMapping("/user_info")
    public PackedObject getUserInfo(@RequestHeader("owner_user_name") String userName){
        UserInfoEntity userInfoEntity=service.getUserInfo(userName);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(userInfoEntity);

        return result;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_UPDATE_USER_INFO)
    @PutMapping("/user_info")
    public PackedObject updateUserInfo(@RequestHeader("owner_user_name") String userName,
                                       @RequestBody PackedObject parameter){
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);

        service.updateUserInfo(userName,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

}
