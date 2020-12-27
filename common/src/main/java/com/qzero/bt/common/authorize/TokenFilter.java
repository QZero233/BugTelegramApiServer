package com.qzero.bt.common.authorize;

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

    @Autowired
    private GlobalUserDetailsService globalUserDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String ownerUserName=httpServletRequest.getHeader("owner_user_name");
        if(ownerUserName==null){
            chain.doFilter(request,response);//We do not check token here
            return;
        }

        UserDetails userDetails=globalUserDetailsService.loadUserByUsername(ownerUserName);
        UsernamePasswordAuthenticationToken securityToken=new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(securityToken);
        chain.doFilter(request,response);
    }


}
