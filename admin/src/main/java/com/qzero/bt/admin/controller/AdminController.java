package com.qzero.bt.admin.controller;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.admin.service.UserManageService;
import com.qzero.bt.common.exception.ResponsiveException;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserManageService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @RequestMapping("/add_user")
    public PackedObject addUser(@RequestBody PackedObject parameter) throws ResponsiveException {
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        service.addUser(tokenEntity,authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @RequestMapping("/delete_user")
    public PackedObject deleteUser(@RequestBody PackedObject parameter) throws ResponsiveException {
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        String userName=parameter.parseObject("username",String.class);

        service.deleteUser(tokenEntity,userName);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @RequestMapping("/update_user")
    public PackedObject updateUser(@RequestBody PackedObject parameter) throws ResponsiveException{
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        service.updateUser(tokenEntity,authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @RequestMapping("/get_all_users")
    public PackedObject getAllUsers(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);

        List<UserInfoForAdmin> result=service.getAllUsers(tokenEntity);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("userList",result);

        return returnValue;
    }

    @RequestMapping("/get_user")
    public PackedObject getUser(@RequestBody PackedObject parameter){
        TokenEntity tokenEntity=parameter.parseObject(TokenEntity.class);
        String userName=parameter.parseObject("username",String.class);

        UserInfoForAdmin result=service.getUser(tokenEntity,userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(result);

        return returnValue;
    }

}
