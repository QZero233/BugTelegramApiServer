package com.qzero.bt.observe;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataObserveRecordDao {

    public static final int DATA_STATUS_ALIVE=0;
    public static final int DATA_STATUS_DELETED=-1;

    public static final int DATA_TYPE_USER_INFO=1;

    @Autowired
    private SessionFactory sessionFactory;

    public void addDataObserveRecord(DataObserveRecordEntity recordEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(recordEntity);
    }

    public void deleteDataObserveRecord(String observeId){
        Session session=sessionFactory.getCurrentSession();

        DataObserveRecordEntity recordEntity=session.load(DataObserveRecordEntity.class,observeId);
        session.delete(recordEntity);
    }

    public void updateDataObserveRecord(DataObserveRecordEntity recordEntity){
        Session session=sessionFactory.getCurrentSession();
        session.merge(recordEntity);
    }

    public DataObserveRecordEntity getRecordByObserveId(String observeId){
        Session session=sessionFactory.getCurrentSession();
        return session.get(DataObserveRecordEntity.class,observeId);
    }

    public List<DataObserveRecordEntity> getRecordsByData(String dataId,int dataType){
        Session session=sessionFactory.getCurrentSession();

        Query query=session.createQuery("from DataObserveRecordEntity where dataId=?1 and dataType=?2");
        query.setParameter(1,dataId);
        query.setParameter(2,dataType);

        return query.getResultList();
    }

    public List<DataObserveRecordEntity> getRecordsByUserNameAndTime(String observerUserName,long lastSyncTime){
        Session session=sessionFactory.getCurrentSession();

        Query query=session.createQuery("from DataObserveRecordEntity where observerUserName=?1 and lastModifiedTime>?2");
        query.setParameter(1,observerUserName);
        query.setParameter(2,lastSyncTime);

        return query.getResultList();
    }

}
