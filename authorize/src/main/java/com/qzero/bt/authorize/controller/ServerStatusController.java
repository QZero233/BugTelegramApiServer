package com.qzero.bt.authorize.controller;

import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.data.ServerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServerStatusController {

    @Autowired
    private ServerStatus currentServerStatus;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @GetMapping("/status")
    public PackedObject requestServerStatus(){
        PackedObject result=packedObjectFactory.getPackedObject();
        result.addObject(currentServerStatus);
        return result;
    }
}
