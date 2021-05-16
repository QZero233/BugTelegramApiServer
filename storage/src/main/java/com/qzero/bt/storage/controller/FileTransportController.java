package com.qzero.bt.storage.controller;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceManager;
import com.qzero.bt.storage.service.FileResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/storage/transport")
public class FileTransportController {

    @Autowired
    private FileResourceService resourceService;

    @Autowired
    private FileResourceManager resourceManager;

    @Autowired
    private IPackedObjectFactory objectFactory;

    private Logger log= LoggerFactory.getLogger(getClass());

    @ResponseBody
    @PostMapping("/{resource_id}")
    public PackedObject uploadFile(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("resource_id") String resourceId,
                                   @RequestParam("offset") Long offset,
                                   @RequestParam("length") Integer length,
                                   @RequestParam("file") MultipartFile file) throws ResponsiveException, IOException {

        if(!resourceService.checkResourcePermission(resourceId,userDetails.getUsername()))
            return objectFactory.getReturnValue(false,"Resource is not accessible");

        FileResource resource=resourceService.getFileResource(resourceId);
        if(resource.getResourceStatus()!=FileResource.STATUS_TRANSPORTING)
            return objectFactory.getReturnValue(false,"Resource is not being transported");

        File storageFile=resourceManager.getResourceFile(resourceId);
        RandomAccessFile randomAccessFile=new RandomAccessFile(storageFile,"rw");

        if(offset>randomAccessFile.length()){
            return objectFactory.getReturnValue(false,"Offset is too long");
        }

        randomAccessFile.seek(offset);
        randomAccessFile.write(file.getBytes(),0,length);
        randomAccessFile.close();

        return objectFactory.getReturnValue(true,null);
    }

    @GetMapping("/{resource_id}")
    public void downloadFile(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("resource_id") String resourceId,
                             @RequestParam("offset") Long offset,
                             @RequestParam("length") Integer length,
                             HttpServletResponse response) throws ResponsiveException, IOException {

        if(!resourceService.checkIfResourceCanBeDownloaded(resourceId,userDetails.getUsername()))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"File resource has not been ready yet");

        RandomAccessFile randomAccessFile=new RandomAccessFile(resourceManager.getResourceFile(resourceId),"r");
        randomAccessFile.seek(offset);

        byte[] buf=new byte[length];
        randomAccessFile.readFully(buf);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=1.txt");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buf);

    }

}
