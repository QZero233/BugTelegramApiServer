package com.qzero.bt.dao;

import com.qzero.bt.data.UserInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        session.merge(userInfoEntity);
    }

    public UserInfoEntity getUserInfo(String userName){
        Session session=sessionFactory.getCurrentSession();
        return session.get(UserInfoEntity.class,userName);
    }

    public List<UserInfoEntity> getAllUserInfo(){
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery("from UserInfoEntity");
        return query.getResultList();
    }
}
