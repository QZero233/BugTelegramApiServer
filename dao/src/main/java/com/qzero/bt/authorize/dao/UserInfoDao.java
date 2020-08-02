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

    public UserInfoEntity getUserInfo(String userName){
        Session session=sessionFactory.getCurrentSession();
        return session.get(UserInfoEntity.class,userName);
    }

}
