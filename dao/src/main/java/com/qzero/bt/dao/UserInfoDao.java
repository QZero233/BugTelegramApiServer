package com.qzero.bt.dao;

import com.qzero.bt.data.UserInfoEntity;
import com.qzero.bt.observe.DataObserveRecordDao;
import com.qzero.bt.observe.aspect.DataChange;
import com.qzero.bt.observe.aspect.EnableDataObserve;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableDataObserve(dataType = DataObserveRecordDao.DATA_TYPE_USER_INFO,idFieldName = "userName")
public class UserInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addUserInfo(UserInfoEntity userInfoEntity){
        Session session=sessionFactory.getCurrentSession();
        session.save(userInfoEntity);
    }

    @DataChange(delete = true)
    public void deleteUserInfo(String userName){
        Session session=sessionFactory.getCurrentSession();
        UserInfoEntity userInfoEntity=session.load(UserInfoEntity.class,userName);
        session.delete(userInfoEntity);
    }

    @DataChange
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
