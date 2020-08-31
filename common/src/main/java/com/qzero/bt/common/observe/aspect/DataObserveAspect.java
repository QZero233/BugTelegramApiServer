package com.qzero.bt.common.observe.aspect;

import com.qzero.bt.common.observe.remind.ObserveRemindUtils;
import com.qzero.bt.observe.DataObserveRecordDao;
import com.qzero.bt.observe.DataObserveRecordEntity;
import com.qzero.bt.observe.aspect.DataChange;
import com.qzero.bt.observe.aspect.EnableDataObserve;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Transactional
@Component
public class DataObserveAspect {

    @Autowired
    private DataObserveRecordDao dao;

    @Pointcut("execution(* com.qzero.bt.dao.*.*(*))")
    private void methodsInDao(){

    }

    @Around("methodsInDao()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        if(joinPoint.getTarget()==null)
            throw new IllegalArgumentException("Can not take static called method");

        int dataType;
        String idFieldName;
        Class target=joinPoint.getTarget().getClass();
        EnableDataObserve enableDataObserve= (EnableDataObserve) target.getDeclaredAnnotation(EnableDataObserve.class);
        if(enableDataObserve==null || !enableDataObserve.enabled())
            return joinPoint.proceed(joinPoint.getArgs());

        dataType=enableDataObserve.dataType();
        idFieldName=enableDataObserve.idFieldName();

        Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
        DataChange autoDataObserve=method.getAnnotation(DataChange.class);
        if(autoDataObserve==null || !autoDataObserve.enabled())
            return joinPoint.proceed(joinPoint.getArgs());

        Object[] args=joinPoint.getArgs();
        if(args.length<1)
            throw new IllegalArgumentException("DataObserveAspect can not take a method with no args");

        Object first=args[0];
        String dataId;
        if(first instanceof String){
            dataId= (String) first;
        }else{
            if(idFieldName==null)
                throw new IllegalArgumentException("Can not find id field name for "+first.getClass().getSimpleName());

            Class clazz=first.getClass();

            Field field=clazz.getDeclaredField(idFieldName);
            field.setAccessible(true);
            dataId= (String) field.get(first);
        }

        List<String> remindedUserList=new ArrayList<>();

        List<DataObserveRecordEntity> recordEntityList=dao.getRecordsByData(dataId,dataType);
        if(recordEntityList!=null){
            for(DataObserveRecordEntity recordEntity:recordEntityList){
                recordEntity=handleChange(recordEntity,autoDataObserve);
                dao.updateDataObserveRecord(recordEntity);

                String userName=recordEntity.getObserverUserName();
                if(!remindedUserList.contains(userName)){
                    ObserveRemindUtils.remindUser(userName);
                    remindedUserList.add(userName);
                }
            }
        }

        return joinPoint.proceed(joinPoint.getArgs());

    }

    private DataObserveRecordEntity handleChange(DataObserveRecordEntity recordEntity, DataChange actionType){
        recordEntity.setLastModifiedTime(System.currentTimeMillis());
        if(actionType.delete()){
            recordEntity.setDataStatus(DataObserveRecordDao.DATA_STATUS_DELETED);
        }

        return recordEntity;
    }

}
