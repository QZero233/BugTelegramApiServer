package com.qzero.bt.common.utils;

import com.qzero.bt.data.TokenEntity;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    public static TokenEntity getTokenByRequest(HttpServletRequest request){
        TokenEntity tokenEntity=new TokenEntity();
        tokenEntity.setTokenId(request.getHeader("tokenId"));
        tokenEntity.setOwnerUserName(request.getHeader("ownerUserName"));
        return tokenEntity;
    }

}
