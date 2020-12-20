package com.qzero.bt.admin.controller;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.admin.service.UserManageService;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserManageService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @PostMapping("/user")
    public PackedObject addUser(@RequestBody PackedObject parameter) throws ResponsiveException {
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        Assert.notNull(userInfoEntity,"User info can not be null");
        Assert.notNull(authorizeInfoEntity,"Authorize info info can not be null");

        service.addUser(authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/user/{user_name}")
    public PackedObject deleteUser(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("user_name") String userName) throws ResponsiveException {
        service.deleteUser(userDetails.getUsername(),userName);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/user")
    public PackedObject updateUser(@RequestBody PackedObject parameter) throws ResponsiveException{
        UserInfoEntity userInfoEntity=parameter.parseObject(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.parseObject(AuthorizeInfoEntity.class);

        Assert.notNull(userInfoEntity,"User info can not be null");
        Assert.notNull(authorizeInfoEntity,"Authorize info info can not be null");

        service.updateUser(authorizeInfoEntity,userInfoEntity);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @GetMapping("/all_users")
    public PackedObject getAllUsers(){
        List<UserInfoForAdmin> result=service.getAllUsers();

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("userList",result);

        return returnValue;
    }

    @GetMapping("/user/{user_name}")
    public PackedObject getUser(@PathVariable("user_name") String userName){
        UserInfoForAdmin result=service.getUser(userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(result);

        return returnValue;
    }

}
