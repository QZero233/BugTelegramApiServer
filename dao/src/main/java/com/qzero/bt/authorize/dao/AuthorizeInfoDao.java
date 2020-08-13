package com.qzero.bt.authorize.dao;

import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addAuthorizeInfo(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(authorizeInfoEntity);
    }

    public void deleteAuthorizeInfo(String username){
        Session session=sessionFactory.getCurrentSession();

        AuthorizeInfoEntity authorizeInfoEntity=session.load(AuthorizeInfoEntity.class,username);
        if(authorizeInfoEntity ==null)
            return;

        session.delete(authorizeInfoEntity);
    }

    public void updateAuthorizeInfo(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.update(authorizeInfoEntity);
    }

    public AuthorizeInfoEntity getAuthorizeInfoByName(String userName){
        Session session=sessionFactory.getCurrentSession();
        return session.get(AuthorizeInfoEntity.class,userName);
    }
}
