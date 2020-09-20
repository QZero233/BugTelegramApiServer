package com.qzero.bt.common.authorize.dao;

import com.qzero.bt.common.authorize.data.TokenEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addToken(TokenEntity tokenEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(tokenEntity);
    }

    public void deleteToken(TokenEntity tokenEntity){
        Session session=sessionFactory.getCurrentSession();
        tokenEntity=session.load(TokenEntity.class,tokenEntity.getTokenId());
        if(tokenEntity==null)
            return;
        session.delete(tokenEntity);
    }

    public void updateToken(TokenEntity tokenEntity){
        Session session=sessionFactory.getCurrentSession();
        session.update(tokenEntity);
    }

    public TokenEntity getTokenById(String tokenId){
        Session session=sessionFactory.getCurrentSession();
        return session.get(TokenEntity.class,tokenId);
    }

    public List<TokenEntity> getAllTokensByOwnerUserName(String ownerUserName){
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery("from TokenEntity where ownerUserName=?1");
        query.setParameter(1,ownerUserName);
        return query.getResultList();
    }

    public void deleteAllTokensLessThanGlobalByOwnerUserName(String ownerUserName){
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery("delete TokenEntity where ownerUserName=?1 and permissionLevel=?2");
        query.setParameter(1,ownerUserName);
        query.setParameter(2,TokenEntity.PERMISSION_LEVEL_APPLICATION);
        query.executeUpdate();
    }

}
