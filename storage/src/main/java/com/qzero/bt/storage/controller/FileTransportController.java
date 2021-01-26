package com.qzero.bt.storage.controller;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceManager;
import com.qzero.bt.storage.data.FileUploadRecord;
import com.qzero.bt.storage.service.FileResourceService;
import com.qzero.bt.storage.service.FileUploadRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/storage/transport")
public class FileTransportController {

    @Autowired
    private FileResourceService resourceService;

    @Autowired
    private FileUploadRecordService recordService;

    @Autowired
    private FileResourceManager resourceManager;

    @Autowired
    private IPackedObjectFactory objectFactory;

    private Logger log= LoggerFactory.getLogger(getClass());

    @ResponseBody
    @PostMapping("/{resource_id}/{block_index}")
    public PackedObject uploadFileBlock(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("resource_id") String resourceId,
                                        @PathVariable("block_index") int blockIndex,
                                        @RequestParam("file") MultipartFile file) throws ResponsiveException, IOException {

        FileUploadRecord record=recordService.getRecord(resourceId);
        if(blockIndex+1>record.getAllBlockCount())
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Block index is overflowed");

        if(blockIndex==record.getEntireBlockCount() && file.getSize()!=record.getRestContentSize())
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Block size is overflowed, expected "+record.getRestContentSize());

        if(blockIndex!=record.getEntireBlockCount() && file.getSize()!=record.getBlockSize())
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Block size is overflowed, expected "+record.getBlockSize());

        File tmpFile=resourceManager.getTempFile(resourceId,blockIndex);
        file.transferTo(tmpFile);

        record.getTransportedBlockIndex().add(blockIndex);
        recordService.updateRecord(record);

        int remainBlock=record.getAllBlockCount()-record.getTransportedBlockIndex().size();

        if(recordService.checkIfUploadFinished(resourceId)){
            resourceManager.mergeTempFile(resourceId);
            resourceManager.deleteTempFiles(resourceId);
            recordService.deleteRecord(resourceId);
            resourceService.updateFileResourceStatus(resourceId, FileResource.STATUS_READY);

            log.debug(String.format("File resource with id %s has fully uploaded and merged", resourceId));
        }

        return objectFactory.getReturnValue(true,String.valueOf(remainBlock));
    }

}
