package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.authorize.data.TokenEntity;
import com.qzero.bt.authorize.exception.ResponsiveException;
import com.qzero.bt.authorize.service.AuthorizeService;
import com.qzero.bt.authorize.view.ExecuteResult;
import com.qzero.bt.authorize.view.JsonView;
import com.qzero.bt.authorize.view.PackedParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private AuthorizeService service;

    @Autowired
    private JsonView jsonView;

    @RequestMapping("/login")
    public ModelAndView login(@RequestBody PackedParameter parameter) throws ResponsiveException {
        TokenEntity tokenEntityForLogin=parameter.getParameter("tokenPreset",TokenEntity.class);
        AuthorizeInfoEntity authorizeInfoEntityForLogin =parameter.getParameter("loginUserInfo", AuthorizeInfoEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);

        TokenEntity tokenEntity=service.login(authorizeInfoEntityForLogin,tokenEntityForLogin);

        if(tokenEntity==null){
            modelAndView.addObject(new ExecuteResult(false,"登录失败，也许是密码错误吧"));
        }else{
            modelAndView.addObject(new ExecuteResult(true,null,tokenEntity));
        }

        return modelAndView;
    }

    @RequestMapping("/get_token_detail")
    public ModelAndView getTokenDetail(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        tokenEntity=service.getTokenById(tokenEntity);
        modelAndView.addObject(new ExecuteResult(true,null,tokenEntity));

        //TODO check if the token is expired

        return modelAndView;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter("logoutToken",TokenEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        service.logout(tokenEntity);
        modelAndView.addObject(new ExecuteResult(true,null));
        return modelAndView;
    }


}
