package com.qzero.bt.storage.service;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceDao;
import com.qzero.bt.storage.data.FileResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class FileResourceService {

    @Autowired
    private FileResourceDao resourceDao;

    @Autowired
    private FileResourceManager resourceManager;

    /**
     *
     * @param fileResource
     * @return The resourceId of the file resource
     */
    public String addFileResource(FileResource fileResource,String operatorUserName) throws IOException {
        String resourceId= UUIDUtils.getRandomUUID();

        fileResource.setResourceId(resourceId);
        fileResource.setOwnerUserName(operatorUserName);
        fileResource.setResourceStatus(FileResource.STATUS_TRANSPORTING);

        resourceDao.save(fileResource);

        resourceManager.createResourceFile(resourceId,fileResource.getResourceLength());

        return resourceId;
    }

    public void deleteFileResource(String resourceId){
        resourceDao.deleteById(resourceId);
    }

    public FileResource getFileResource(String resourceId){
        if(!resourceDao.existsById(resourceId))
            return null;

        return resourceDao.getOne(resourceId);
    }

    public List<FileResource> getAllFileResources(String userName){
        return resourceDao.findAllByOwnerUserName(userName);
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
        if(resource.getResourceStatus()!=FileResource.STATUS_READY)
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"File resource has not been ready yet");

        return true;
    }

}
