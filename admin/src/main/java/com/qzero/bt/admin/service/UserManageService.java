package com.qzero.bt.admin.service;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.common.authorize.dao.AuthorizeInfoRepository;
import com.qzero.bt.common.authorize.dao.TokenRepository;
import com.qzero.bt.common.authorize.dao.UserInfoRepository;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.qzero.bt.common.exception.ErrorCodeList.*;

@Service
@Transactional
public class UserManageService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthorizeInfoRepository authorizeInfoRepository;

    @Autowired
    private TokenRepository tokenRepository;


    public void addUser(AuthorizeInfoEntity authorizeInfo, UserInfoEntity userInfo) throws ResponsiveException {
        if(!authorizeInfo.getUserName().equals(userInfo.getUserName()))
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Multi username");

        //Check if the hash is valid
        if(!authorizeInfo.getCodeHash().matches("^(.{2}:){31}.{2}$") ||
            !authorizeInfo.getPasswordHash().matches("^(.{2}:){31}.{2}$")){
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Invalid hash");
        }

        authorizeInfoRepository.save(authorizeInfo);
        userInfoRepository.save(userInfo);;
    }


    public void deleteUser(String operatorName,String username) throws ResponsiveException {
        UserInfoEntity userInfoEntity=userInfoRepository.getOne(username);
        if(userInfoEntity==null)
            throw new ResponsiveException(CODE_MISSING_RESOURCE,"User does not exist");

        //Can not delete yourself
        if(operatorName.equals(username)){
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Can not delete yourself");
        }

        //System admin can not be deleted
        if(userInfoEntity.getGroupLevel()>=UserInfoEntity.GROUP_SYSTEM_ADMIN){
            throw new ResponsiveException(CODE_PERMISSION_DENIED,"Can not delete system admin");
        }

        userInfoRepository.deleteById(username);
        authorizeInfoRepository.deleteById(username);
    }


    public void updateUser(AuthorizeInfoEntity newAuthorizeInfo,UserInfoEntity newUserInfo) throws ResponsiveException {
        if(!newAuthorizeInfo.getUserName().equals(newUserInfo.getUserName()))
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Multi username");

        UserInfoEntity origin=userInfoRepository.getOne(newUserInfo.getUserName());
        Assert.notNull(origin,"User does not exist");
        AuthorizeInfoEntity originAuthorizeInfo=authorizeInfoRepository.getOne(newAuthorizeInfo.getUserName());
        Assert.notNull(originAuthorizeInfo,"User does not exist");

        //Can not upgrade to system admin
        if(origin.getGroupLevel()<UserInfoEntity.GROUP_SYSTEM_ADMIN &&
                newUserInfo.getGroupLevel()>=UserInfoEntity.GROUP_SYSTEM_ADMIN){
            throw new ResponsiveException(CODE_PERMISSION_DENIED,"Can not upgrade to system admin");
        }

        //System admin can not update group level
        if(origin.getGroupLevel()==UserInfoEntity.GROUP_SYSTEM_ADMIN && origin.getGroupLevel()!=newUserInfo.getGroupLevel()){
            throw new ResponsiveException(CODE_PERMISSION_DENIED,"System admin can not update group level");
        }

        //If freeze,delete tokens
        if(newAuthorizeInfo.getAuthorizeStatus()==AuthorizeInfoEntity.STATUS_FREEZING){
            tokenRepository.deleteByOwnerUserNameAndPermissionLevelLessThan(newUserInfo.getUserName(),
                    TokenEntity.PERMISSION_LEVEL_GLOBAL);
        }
        //Admin can not update codeHash and passwordHash
        newAuthorizeInfo.setCodeHash(originAuthorizeInfo.getCodeHash());
        newAuthorizeInfo.setPasswordHash(originAuthorizeInfo.getPasswordHash());

        authorizeInfoRepository.save(newAuthorizeInfo);
        userInfoRepository.save(newUserInfo);
    }


    public List<UserInfoForAdmin> getAllUsers(){
        List<UserInfoForAdmin> result=new ArrayList<>();

        List<UserInfoEntity> userInfoEntityList=userInfoRepository.findAll();
        for(UserInfoEntity userInfoEntity:userInfoEntityList){
            AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoRepository.getOne(userInfoEntity.getUserName());
            UserInfoForAdmin userInfoForAdmin=new UserInfoForAdmin(userInfoEntity,authorizeInfoEntity.getAuthorizeStatus());
            result.add(userInfoForAdmin);
        }

        return result;
    }


    public UserInfoForAdmin getUser(String userName){
        UserInfoEntity userInfoEntity=userInfoRepository.getOne(userName);
        AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoRepository.getOne(userName);

        return new UserInfoForAdmin(userInfoEntity,authorizeInfoEntity.getAuthorizeStatus());
    }

}
