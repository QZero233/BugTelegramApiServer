package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AccountModifyService;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//FIXME DID NOT VERIFY TOKEN PERMISSION
@RestController
@RequestMapping("/account")
public class AccountModifyController {

    @Autowired
    private AccountModifyService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @GetMapping("/token_list}")
    public PackedObject getTokenList(@AuthenticationPrincipal UserDetails userDetails){
        List<TokenEntity> tokenEntityListProxy=service.getTokenList(userDetails.getUsername());
        PackedObject result;
        if(tokenEntityListProxy==null){
            result=packedObjectFactory.getReturnValue(false,null);
            return result;
        }

        List<TokenEntity> tokenEntityList=new ArrayList<>();
        for(TokenEntity tokenEntity:tokenEntityListProxy){
            tokenEntityList.add(Hibernate.unproxy(tokenEntity,TokenEntity.class));
        }

        result=packedObjectFactory.getReturnValue(true,null);
        result.addObject("tokenList",tokenEntityList);
        return result;
    }

    @PutMapping("/password")
    public PackedObject changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam("new_password_hash")String newPasswordHash){
        service.changePassword(userDetails.getUsername(), newPasswordHash);
        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/account_frozen")
    public PackedObject freezeAccount(@AuthenticationPrincipal UserDetails userDetails){
        service.freezeAccount(userDetails.getUsername());
        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/account_unfrozen")
    public PackedObject unfreezeAccount(@AuthenticationPrincipal UserDetails userDetails){
        service.unfreezeAccount(userDetails.getUsername());
        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/user_info/account_status_and_motto")
    public PackedObject updateAccountStatusAndMotto(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam("account_status") int accountStatus,
                                                    @RequestParam("motto") String motto) throws ResponsiveException {
        UserInfoEntity userInfoEntity=service.getUserInfo(userDetails.getUsername());
        userInfoEntity.setAccountStatus(accountStatus);
        userInfoEntity.setMotto(motto);
        service.updateUserInfo(userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @GetMapping("/user_info/{user_name}")
    public PackedObject getOtherUserInfo(@PathVariable("user_name")String userName){
        UserInfoEntity userInfoEntity=service.getUserInfo(userName);
        if(userInfoEntity==null)
            return packedObjectFactory.getReturnValue(new ActionResult(false, ErrorCodeList.CODE_MISSING_RESOURCE,""));

        userInfoEntity= (UserInfoEntity) Hibernate.unproxy(userInfoEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(userInfoEntity);

        return result;
    }

}
