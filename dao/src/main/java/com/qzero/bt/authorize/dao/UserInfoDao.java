package com.qzero.bt.authorize.dao;

import com.qzero.bt.authorize.data.UserInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addUserInfo(UserInfoEntity userInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(userInfoEntity);
    }

    public void deleteUserInfo(String userName){
        Session session=sessionFactory.getCurrentSession();
        UserInfoEntity userInfoEntity=session.load(UserInfoEntity.class,userName);
        session.delete(userInfoEntity);
    }
    public void updateUserInfo(UserInfoEntity userInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.update(userInfoEntity);
    }

    public UserInfoEntity getUserInfo(String userName){
        Session session=sessionFactory.getCurrentSession();
        return session.get(UserInfoEntity.class,userName);
    }
}
