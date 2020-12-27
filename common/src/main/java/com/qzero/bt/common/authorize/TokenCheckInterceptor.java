package com.qzero.bt.common.authorize;

import com.qzero.bt.common.authorize.dao.TokenRepository;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.PermissionDeniedException;
import com.qzero.bt.common.exception.ResourceDoesNotExistException;
import com.qzero.bt.common.exception.ResponsiveException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Transactional
@Component
public class TokenCheckInterceptor implements HandlerInterceptor {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getServletPath().equals("/authorize/login")){
            return true;
        }

        String tokenId=request.getHeader("token_id");
        String ownerUserName=request.getHeader("owner_user_name");

        if(tokenId==null || ownerUserName==null)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"TokenId and OwnerUserName can not be empty");

        if(!tokenRepository.existsById(tokenId))
            throw new ResourceDoesNotExistException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token does not exist");

        TokenEntity tokenEntity=tokenRepository.getOne(tokenId);

        if(!tokenEntity.getOwnerUserName().equals(ownerUserName))
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token owner name does not match");

        if(tokenEntity.getEndTime()>0 && tokenEntity.getEndTime()<System.currentTimeMillis()){
            tokenRepository.deleteById(tokenId);
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token expired");
        }

        log.trace("Token valid");

        return true;
    }
}
