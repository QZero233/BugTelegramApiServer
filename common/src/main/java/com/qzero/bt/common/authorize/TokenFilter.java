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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Transactional
@Component
public class TokenFilter extends GenericFilter {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private GlobalUserDetailsService globalUserDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String tokenId=httpServletRequest.getHeader("token_id");
            String ownerUserName=httpServletRequest.getHeader("owner_user_name");

            if(tokenId==null || ownerUserName==null)
                throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"TokenId and OwnerUserName can not be empty");

            TokenEntity tokenEntity=tokenRepository.getOne(tokenId);
            if(tokenEntity==null)
                throw new ResourceDoesNotExistException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token does not exist");

            if(!tokenEntity.getOwnerUserName().equals(ownerUserName))
                throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token owner name does not match");

            if(tokenEntity.getEndTime()>0 && tokenEntity.getEndTime()<System.currentTimeMillis()){
                tokenRepository.delete(tokenEntity);
                throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token expired");
            }

            log.trace("Token valid");

            UserDetails userDetails=globalUserDetailsService.loadUserByUsername(ownerUserName);
            UsernamePasswordAuthenticationToken securityToken=new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(securityToken);
        }catch (Exception e){
            log.error("Failed to check token",e);
        }
        chain.doFilter(request,response);
    }
}
