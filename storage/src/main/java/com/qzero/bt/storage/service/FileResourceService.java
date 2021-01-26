package com.qzero.bt.storage.service;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceDao;
import com.qzero.bt.storage.data.FileUploadRecord;
import com.qzero.bt.storage.data.FileUploadRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileResourceService {

    @Autowired
    private FileResourceDao resourceDao;

    @Autowired
    private FileUploadRecordDao recordDao;

    /**
     *
     * @param fileResource
     * @return The resourceId of the file resource
     */
    public String addFileResourceAndTransportTask(FileResource fileResource,long transportBlockSize,String operatorUserName) throws ResponsiveException {
        if(transportBlockSize>= FileUploadRecord.MAX_TRANSPORT_SIZE)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Block size is more than 10MB");

        String resourceId= UUIDUtils.getRandomUUID();

        fileResource.setResourceId(resourceId);
        fileResource.setOwnerUserName(operatorUserName);
        fileResource.setResourceStatus(FileResource.STATUS_TRANSPORTING);

        FileUploadRecord record=new FileUploadRecord();
        record.setResourceId(resourceId);
        record.setBlockSize(transportBlockSize);
        record.setEntireBlockCount(TransportBlockCalculator.getEntireBlockCount(fileResource.getResourceLength(),transportBlockSize));
        record.setFileLength(fileResource.getResourceLength());
        record.setRestContentSize(TransportBlockCalculator.getRestContentSize(fileResource.getResourceLength(),transportBlockSize));

        resourceDao.save(fileResource);
        recordDao.save(record);

        return resourceId;
    }

    public void deleteFileResource(String resourceId){
        resourceDao.deleteById(resourceId);
    }

    public void updateFileResourceStatus(String resourceId,int newStatus) throws ResponsiveException {
        if(!resourceDao.existsById(resourceId))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER, String.format("File resource with id %s does not exists", resourceId));

        FileResource resource=resourceDao.getOne(resourceId);
        resource.setResourceStatus(newStatus);
        resourceDao.save(resource);
    }

    public boolean checkResourcePermission(String resourceId, String operatorName) throws ResponsiveException {
        if(!resourceDao.existsById(resourceId))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER, String.format("File resource with id %s does not exists", resourceId));

        FileResource resource=resourceDao.getOne(resourceId);
        if(!resource.getOwnerUserName().equals(operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You have no permission to file resource with id "+resourceId);

        return true;
    }

    public boolean checkIfResourceCanBeDownloaded(String resourceId,String operatorName) throws ResponsiveException {
        if(!checkResourcePermission(resourceId,operatorName))
            return false;

        FileResource resource=resourceDao.getOne(resourceId);
        return resource.getResourceStatus()==FileResource.STATUS_READY;
    }

}
