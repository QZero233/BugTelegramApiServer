package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AccountModifyService;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.ExecuteResult;
import com.qzero.bt.common.view.JsonView;
import com.qzero.bt.common.view.PackedParameter;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.TokenEntity;
import com.qzero.bt.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@RequestMapping("/account")
public class AccountModifyController {

    @Autowired
    private AccountModifyService service;

    @Autowired
    private JsonView jsonView;

    @RequestMapping("/get_token_list")
    public ModelAndView getTokenList(@RequestBody PackedParameter parameter){
        ModelAndView modelAndView=new ModelAndView(jsonView);

        TokenEntity userToken=parameter.getParameter(TokenEntity.class);

        List<TokenEntity> tokenEntityList=service.getTokenList(userToken);
        if(tokenEntityList==null){
            modelAndView.addObject(new ExecuteResult(false,null));
        }else{
            PackedParameter packedParameter=new PackedParameter();
            packedParameter.addParameter("tokenList",tokenEntityList);
            ExecuteResult result=new ExecuteResult(new ActionResult(true,null),packedParameter);
            modelAndView.addObject(result);
        }

        return modelAndView;
    }

    @RequestMapping("/change_password")
    public ModelAndView changePassword(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        AuthorizeInfoEntity authorizeInfoEntity =parameter.getParameter(AuthorizeInfoEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        service.changePassword(tokenEntity, authorizeInfoEntity);
        modelAndView.addObject(new ExecuteResult(true,null));
        return modelAndView;
    }


    @RequestMapping("/freeze_account")
    public ModelAndView freezeAccount(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        service.freezeAccount(tokenEntity);
        modelAndView.addObject(new ExecuteResult(true,null));
        return modelAndView;
    }

    @RequestMapping("/unfreeze_account")
    public ModelAndView unfreezeAccount(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        service.unfreezeAccount(tokenEntity);
        modelAndView.addObject(new ExecuteResult(true,null));
        return modelAndView;
    }

    @RequestMapping("/get_user_info")
    public ModelAndView getUserInfo(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        UserInfoEntity userInfoEntity=service.getUserInfo(tokenEntity);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,null,userInfoEntity));
        return modelAndView;
    }

    @RequestMapping("/update_user_info")
    public ModelAndView updateUserInfo(@RequestBody PackedParameter parameter){
        TokenEntity tokenEntity=parameter.getParameter(TokenEntity.class);
        UserInfoEntity userInfoEntity=parameter.getParameter(UserInfoEntity.class);

        service.updateUserInfo(tokenEntity,userInfoEntity);

        ModelAndView modelAndView=new ModelAndView(jsonView);
        modelAndView.addObject(new ExecuteResult(true,null));
        return modelAndView;
    }

}
