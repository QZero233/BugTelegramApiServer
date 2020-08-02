package com.qzero.bt.authorize.dao;

import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizeInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addUserInfo(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(authorizeInfoEntity);
    }

    public void deleteUserInfo(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        authorizeInfoEntity =session.load(AuthorizeInfoEntity.class, authorizeInfoEntity.getUserName());
        if(authorizeInfoEntity ==null)
            return;
        session.delete(authorizeInfoEntity);
    }

    public AuthorizeInfoEntity getUserByName(String userName){
        Session session=sessionFactory.getCurrentSession();
        return session.get(AuthorizeInfoEntity.class,userName);
    }

    public AuthorizeInfoEntity getUserByNameAndCode(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery("from AuthorizeInfoEntity where userName=?1 and codeHash=?2");
        query.setParameter(1, authorizeInfoEntity.getUserName());
        query.setParameter(2, authorizeInfoEntity.getCodeHash());

        List result=query.getResultList();
        if(result==null || result.isEmpty())
            return null;
        return (AuthorizeInfoEntity) result.get(0);
    }

    public AuthorizeInfoEntity getUserByNameAndPassword(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery("from AuthorizeInfoEntity where userName=?1 and passwordHash=?2");
        query.setParameter(1, authorizeInfoEntity.getUserName());
        query.setParameter(2, authorizeInfoEntity.getPasswordHash());

        List result=query.getResultList();
        if(result==null || result.isEmpty())
            return null;
        return (AuthorizeInfoEntity) result.get(0);
    }

    public void updatePassword(AuthorizeInfoEntity authorizeInfoEntity){
        String userName= authorizeInfoEntity.getUserName();
        String newPasswordHash= authorizeInfoEntity.getPasswordHash();

        Session session=sessionFactory.getCurrentSession();
        authorizeInfoEntity =session.load(AuthorizeInfoEntity.class,userName);
        authorizeInfoEntity.setPasswordHash(newPasswordHash);
        session.update(authorizeInfoEntity);
    }

    public void updateUserInfo(AuthorizeInfoEntity authorizeInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.update(authorizeInfoEntity);
    }



}
