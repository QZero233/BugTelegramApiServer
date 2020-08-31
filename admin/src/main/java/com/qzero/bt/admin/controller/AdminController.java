package com.qzero.bt.admin.controller;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.admin.service.UserManageService;
import com.qzero.bt.common.exception.ResponsiveException;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserManageService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @PermissionCheck(PermissionNameList.PERMISSION_MANAGE_USER)
    @PostMapping("/user")
    public PackedObject addUser(@RequestBody PackedObject parameter) throws ResponsiveException {
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        service.addUser(authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MANAGE_USER)
    @DeleteMapping("/user/{user_name}")
    public PackedObject deleteUser(@RequestHeader("owner_user_name") String operatorName,
                                   @PathVariable("user_name") String userName) throws ResponsiveException {
        service.deleteUser(operatorName,userName);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MANAGE_USER)
    @PutMapping("/user")
    public PackedObject updateUser(@RequestBody PackedObject parameter) throws ResponsiveException{
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        service.updateUser(authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MANAGE_USER)
    @GetMapping("/all_users")
    public PackedObject getAllUsers(){
        List<UserInfoForAdmin> result=service.getAllUsers();

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("userList",result);

        return returnValue;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MANAGE_USER)
    @GetMapping("/user/{user_name}")
    public PackedObject getUser(@PathVariable("user_name") String userName){
        UserInfoForAdmin result=service.getUser(userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(result);

        return returnValue;
    }

}
