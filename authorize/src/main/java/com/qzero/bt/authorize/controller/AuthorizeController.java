package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AuthorizeService;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.ExecuteResult;
import com.qzero.bt.common.view.JsonView;
import com.qzero.bt.common.view.PackedParameter;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
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

    @RequestMapping("/get_authorize_status")
    public ModelAndView getAuthorizeStatus(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);

        int status=service.getAuthorizeStatus(tokenEntity);

        PackedParameter packedParameter=new PackedParameter();
        packedParameter.addParameter("authorizeStatus",status);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(new ActionResult(true,null),packedParameter));
        return modelAndView;
    }

}
