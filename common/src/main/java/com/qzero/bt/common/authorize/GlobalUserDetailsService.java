package com.qzero.bt.common.authorize;

import com.qzero.bt.common.authorize.dao.AuthorizeInfoRepository;
import com.qzero.bt.common.authorize.dao.UserInfoRepository;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class GlobalUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthorizeInfoRepository authorizeInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if(!authorizeInfoRepository.existsByUserName(userName))
            throw new UsernameNotFoundException(String.format("User %s does not exist", userName));

        AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoRepository.getOne(userName);
        UserInfoEntity userInfoEntity=userInfoRepository.getOne(userName);

        Collection<GrantedAuthority> grantedAuthorityCollection=new ArrayList<>();

        int groupLevel=userInfoEntity.getGroupLevel();
        switch (groupLevel){
            case UserInfoEntity.GROUP_USER:
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case UserInfoEntity.GROUP_ADMIN:
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_USER"));
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case UserInfoEntity.GROUP_SYSTEM_ADMIN:
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_USER"));
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                grantedAuthorityCollection.add(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"));
                break;
        }

        return new User(userName,authorizeInfoEntity.getPasswordHash(),true,true,true,
                authorizeInfoEntity.getAuthorizeStatus()!=AuthorizeInfoEntity.STATUS_FREEZING
                ,grantedAuthorityCollection);
    }


}
