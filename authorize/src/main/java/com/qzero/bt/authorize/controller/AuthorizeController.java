package com.qzero.bt.authorize.controller;

import com.qzero.bt.authorize.service.AuthorizeService;
import com.qzero.bt.common.authorize.GlobalUserDetailsService;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.ActionResult;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private AuthorizeService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @Autowired
    private GlobalUserDetailsService globalUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public PackedObject login(@RequestBody PackedObject parameter) throws ResponsiveException {
        TokenEntity tokenEntityForLogin=parameter.parseObject("tokenPreset",TokenEntity.class);
        AuthorizeInfoEntity authorizeInfoEntityForLogin =parameter.parseObject("loginUserInfo", AuthorizeInfoEntity.class);

        TokenEntity tokenEntity=service.login(authorizeInfoEntityForLogin,tokenEntityForLogin);

        UserDetails userDetails=globalUserDetailsService.loadUserByUsername(authorizeInfoEntityForLogin.getUserName());
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

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

    @GetMapping("/token_detail/{token_id}")
    public PackedObject getTokenDetail(@PathVariable("token_id") String tokenId) throws ResponsiveException {
        TokenEntity tokenEntity=service.getTokenById(tokenId);

        if(tokenEntity.getEndTime()<=System.currentTimeMillis()){
            service.logout(tokenEntity);
            throw new ResponsiveException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token is expired");
        }

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject(tokenEntity);

        return result;
    }

    @DeleteMapping("/logout/{token_id}")
    public PackedObject logout(@PathVariable("token_id") String tokenId){
        TokenEntity tokenEntity=new TokenEntity();
        tokenEntity.setTokenId(tokenId);
        service.logout(tokenEntity);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        return result;
    }

    @GetMapping("/authorize_status/{user_name}")
    public PackedObject getAuthorizeStatus(@PathVariable("user_name") String userName){
        int status=service.getAuthorizeStatus(userName);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);

        result.addObject("authorizeStatus",status);

        return result;
    }

}
