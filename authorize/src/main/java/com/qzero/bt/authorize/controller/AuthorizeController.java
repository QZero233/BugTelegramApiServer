package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AuthorizeService;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.permission.PermissionCheck;
import com.qzero.bt.common.permission.PermissionNameList;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private AuthorizeService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @PostMapping("/login")
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


    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_TOKEN)
    @GetMapping("/token_detail/{token_id}")
    public PackedObject getTokenDetail(@PathVariable("token_id") String tokenId){
        TokenEntity tokenEntity=service.getTokenById(tokenId);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(tokenEntity);

        //TODO check if the token is expired

        return result;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_TOKEN)
    @DeleteMapping("/logout/{token_id}")
    public PackedObject logout(@PathVariable("token_id") String tokenId){
        TokenEntity tokenEntity=new TokenEntity();
        tokenEntity.setTokenId(tokenId);
        service.logout(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        return result;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_READ_USER_INFO)
    @GetMapping("/authorize_status/{user_name}")
    public PackedObject getAuthorizeStatus(@PathVariable("user_name") String userName){
        int status=service.getAuthorizeStatus(userName);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        result.addObject("authorizeStatus",status);

        return result;
    }

}
