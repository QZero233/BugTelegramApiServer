package com.qzero.bt.common.observe.service;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.observe.DataObserveRecordDao;
import com.qzero.bt.observe.DataObserveRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ObserveService {

    @Autowired
    private DataObserveRecordDao dao;

    public List<DataObserveRecordEntity> getRecords(String userName,long lastSyncTime){
        return dao.getRecordsByUserNameAndTime(userName,lastSyncTime);
    }

    public void deleteRecord(String userName,String observeId) throws ResponsiveException {
        DataObserveRecordEntity recordEntity=dao.getRecordByObserveId(observeId);
        if(!userName.equals(recordEntity.getObserverUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"Have no permission in deleting record not belonging to you");
        dao.deleteDataObserveRecord(observeId);
    }

}
