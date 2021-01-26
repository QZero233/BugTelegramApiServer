package com.qzero.bt.storage.controller;

import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceManager;
import com.qzero.bt.storage.service.FileResourceService;
import com.qzero.bt.storage.service.FileUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storage/resource")
public class FileTransportTaskController {

    @Autowired
    private FileResourceService resourceService;

    @Autowired
    private FileUploadRecordService recordService;

    @Autowired
    private FileResourceManager resourceManager;

    @Autowired
    private IPackedObjectFactory objectFactory;

    @PostMapping("/")
    public PackedObject newFileResource(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody PackedObject parameter,
                                        @RequestParam("transport_block_size")Long transportBlockSize) throws ResponsiveException {

        FileResource fileResource=parameter.parseObject(FileResource.class);
        String resourceId=resourceService.addFileResourceAndTransportTask(fileResource,transportBlockSize,userDetails.getUsername());

        return objectFactory.getReturnValue(true,resourceId);
    }

    @DeleteMapping("/{resource_id}")
    public PackedObject deleteFileResource(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable("resource_id") String resourceId) throws ResponsiveException {
        if(!resourceService.checkResourcePermission(resourceId,userDetails.getUsername()))
            return objectFactory.getReturnValue(false,"Resource is not accessible");
        resourceService.deleteFileResource(resourceId);
        resourceManager.deleteResourceFile(resourceId);

        if(recordService.isRecordExist(resourceId)){
            resourceManager.deleteTempFiles(resourceId);
            recordService.deleteRecord(resourceId);
        }

        return objectFactory.getReturnValue(true,null);
    }



}
