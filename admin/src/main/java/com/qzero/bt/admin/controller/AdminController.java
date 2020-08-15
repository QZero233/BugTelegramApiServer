package com.qzero.bt.admin.controller;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.admin.service.UserManageService;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import com.qzero.bt.data.UserInfoEntity;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.ExecuteResult;
import com.qzero.bt.common.view.JsonView;
import com.qzero.bt.common.view.PackedParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserManageService service;

    @Autowired
    private JsonView jsonView;

    @RequestMapping("/add_user")
    public ModelAndView addUser(@RequestBody PackedParameter parameter) throws ResponsiveException {
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.getParameter(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.getParameter(AuthorizeInfoEntity.class);

        service.addUser(tokenEntity,authorizeInfoEntity,userInfoEntity);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,""));
        return modelAndView;
    }

    @RequestMapping("/delete_user")
    public ModelAndView deleteUser(@RequestBody PackedParameter parameter) throws ResponsiveException {
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        String userName=parameter.getParameter("username",String.class);

        service.deleteUser(tokenEntity,userName);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,""));
        return modelAndView;
    }

    @RequestMapping("/update_user")
    public ModelAndView updateUser(@RequestBody PackedParameter parameter) throws ResponsiveException{
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.getParameter(UserInfoEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity=parameter.getParameter(AuthorizeInfoEntity.class);

        service.updateUser(tokenEntity,authorizeInfoEntity,userInfoEntity);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,""));
        return modelAndView;
    }

    @RequestMapping("/get_all_users")
    public ModelAndView getAllUsers(@RequestBody PackedParameter parameter) throws ResponsiveException{
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);

        List<UserInfoForAdmin> result=service.getAllUsers(tokenEntity);

        ModelAndView modelAndView=new ModelAndView(jsonView);

        PackedParameter packedParameter=new PackedParameter();
        packedParameter.addParameter("userList",result);

        modelAndView.addObject(new ExecuteResult(new ActionResult(true,null),packedParameter));
        return modelAndView;
    }

    @RequestMapping("/get_user")
    public ModelAndView getUser(@RequestBody PackedParameter parameter) throws ResponsiveException{
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        String userName=parameter.getParameter("username",String.class);

        UserInfoForAdmin result=service.getUser(tokenEntity,userName);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,"",result));
        return modelAndView;
    }

}
