package com.qzero.bt.storage.service;

import com.qzero.bt.storage.data.FileUploadRecord;
import com.qzero.bt.storage.data.FileUploadRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileUploadRecordService {

    @Autowired
    private FileUploadRecordDao recordDao;

    public boolean checkIfUploadFinished(String resourceId){
        if(!recordDao.existsById(resourceId))
            return true;

        FileUploadRecord record=recordDao.getOne(resourceId);
        List<Integer> transported=record.getTransportedBlockIndex();
        if(transported==null || transported.isEmpty())
            return false;

        if(transported.size()==record.getAllBlockCount())
            return true;

        return false;
    }

    public FileUploadRecord getRecord(String resourceId){
        if(!recordDao.existsById(resourceId))
            return null;

        return recordDao.getOne(resourceId);
    }

    public void deleteRecord(String resourceId){
        recordDao.deleteById(resourceId);
    }

    public boolean isRecordExist(String resourceId){
        return recordDao.existsById(resourceId);
    }

    public void updateRecord(FileUploadRecord record){
        recordDao.save(record);
    }

}
