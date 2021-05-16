package com.qzero.bt.storage.controller;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.storage.data.FileResource;
import com.qzero.bt.storage.data.FileResourceManager;
import com.qzero.bt.storage.service.FileResourceService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/storage/resource")
public class FileResourceController {

    @Autowired
    private FileResourceService resourceService;

    @Autowired
    private FileResourceManager resourceManager;

    @Autowired
    private IPackedObjectFactory objectFactory;

    @PostMapping("/")
    public PackedObject newFileResource(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody PackedObject parameter) throws IOException {

        FileResource fileResource=parameter.parseObject(FileResource.class);
        String resourceId=resourceService.addFileResource(fileResource,userDetails.getUsername());

        return objectFactory.getReturnValue(true,resourceId);
    }

    @DeleteMapping("/{resource_id}")
    public PackedObject deleteFileResource(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable("resource_id") String resourceId) throws ResponsiveException {
        if(!resourceService.checkResourcePermission(resourceId,userDetails.getUsername()))
            return objectFactory.getReturnValue(false,"Resource is not accessible");
        resourceService.deleteFileResource(resourceId);
        resourceManager.deleteResourceFile(resourceId);

        return objectFactory.getReturnValue(true,null);
    }

    @GetMapping("/{resource_id}")
    public PackedObject getFileResourceInfo(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("resource_id") String resourceId) throws ResponsiveException {

        if(!resourceService.checkResourcePermission(resourceId,userDetails.getUsername()))
            return objectFactory.getReturnValue(false,"Resource is not accessible");

        FileResource resource=resourceService.getFileResource(resourceId);
        if(resource==null)
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"File resource does not found");

        resource= Hibernate.unproxy(resource,FileResource.class);

        PackedObject returnValue=objectFactory.getReturnValue(true,null);
        returnValue.addObject(resource);
        return returnValue;
    }

    @GetMapping("/")
    public PackedObject getAllFileResources(@AuthenticationPrincipal UserDetails userDetails){
        List<FileResource> resources=resourceService.getAllFileResources(userDetails.getUsername());

        List<FileResource> result=new ArrayList<>();
        if(resources!=null){
            for(FileResource resource:resources){
                result.add(Hibernate.unproxy(resource,FileResource.class));
            }
        }

        PackedObject returnValue=objectFactory.getReturnValue(true,null);
        returnValue.addObject("FileResourceList",result);
        return returnValue;
    }

}
