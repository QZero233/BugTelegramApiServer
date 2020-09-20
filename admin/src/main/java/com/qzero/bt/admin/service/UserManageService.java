package com.qzero.bt.admin.service;

import com.qzero.bt.admin.data.UserInfoForAdmin;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.common.authorize.dao.UserInfoDao;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.qzero.bt.common.exception.ErrorCodeList.*;

@Service
@Transactional
public class UserManageService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private AuthorizeInfoDao authorizeInfoDao;


    public void addUser(AuthorizeInfoEntity authorizeInfo, UserInfoEntity userInfo) throws ResponsiveException {
        if(!authorizeInfo.getUserName().equals(userInfo.getUserName()))
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Multi username");

        //Check if the hash is valid
        if(!authorizeInfo.getCodeHash().matches("^(.{2}:){31}.{2}$") ||
            !authorizeInfo.getPasswordHash().matches("^(.{2}:){31}.{2}$")){
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Invalid hash");
        }

        authorizeInfoDao.addAuthorizeInfo(authorizeInfo);
        userInfoDao.addUserInfo(userInfo);
    }


    public void deleteUser(String operatorName,String username) throws ResponsiveException {
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo(username);
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


        userInfoDao.deleteUserInfo(username);
        authorizeInfoDao.deleteAuthorizeInfo(username);
    }


    public void updateUser(AuthorizeInfoEntity newAuthorizeInfo,UserInfoEntity newUserInfo) throws ResponsiveException {
        //FIXME if freeze,delete tokens
        if(!newAuthorizeInfo.getUserName().equals(newUserInfo.getUserName()))
            throw new ResponsiveException(CODE_BAD_REQUEST_PARAMETER,"Multi username");

        //Can not upgrade to system admin
        UserInfoEntity origin=userInfoDao.getUserInfo(newUserInfo.getUserName());
        if(origin.getGroupLevel()<UserInfoEntity.GROUP_SYSTEM_ADMIN &&
                newUserInfo.getGroupLevel()>=UserInfoEntity.GROUP_SYSTEM_ADMIN){
            throw new ResponsiveException(CODE_PERMISSION_DENIED,"Can not upgrade to system admin");
        }

        //System admin can not update group level
        if(origin.getGroupLevel()==UserInfoEntity.GROUP_SYSTEM_ADMIN && origin.getGroupLevel()!=newUserInfo.getGroupLevel()){
            throw new ResponsiveException(CODE_PERMISSION_DENIED,"System admin can not update group level");
        }

        //Admin can not update codeHash and passwordHash
        AuthorizeInfoEntity originAuthorizeInfo=authorizeInfoDao.getAuthorizeInfoByName(newAuthorizeInfo.getUserName());
        newAuthorizeInfo.setCodeHash(originAuthorizeInfo.getCodeHash());
        newAuthorizeInfo.setPasswordHash(originAuthorizeInfo.getPasswordHash());

        authorizeInfoDao.updateAuthorizeInfo(newAuthorizeInfo);
        userInfoDao.updateUserInfo(newUserInfo);
    }


    public List<UserInfoForAdmin> getAllUsers(){
        List<UserInfoForAdmin> result=new ArrayList<>();

        List<UserInfoEntity> userInfoEntityList=userInfoDao.getAllUserInfo();
        for(UserInfoEntity userInfoEntity:userInfoEntityList){
            AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoDao.getAuthorizeInfoByName(userInfoEntity.getUserName());
            UserInfoForAdmin userInfoForAdmin=new UserInfoForAdmin(userInfoEntity,authorizeInfoEntity.getAuthorizeStatus());
            result.add(userInfoForAdmin);
        }

        return result;
    }


    public UserInfoForAdmin getUser(String userName){
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo(userName);
        AuthorizeInfoEntity authorizeInfoEntity=authorizeInfoDao.getAuthorizeInfoByName(userName);

        return new UserInfoForAdmin(userInfoEntity,authorizeInfoEntity.getAuthorizeStatus());
    }

}
