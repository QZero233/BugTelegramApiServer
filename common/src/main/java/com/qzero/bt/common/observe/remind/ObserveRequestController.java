package com.qzero.bt.common.observe.remind;

import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observe")
@Transactional
public class ObserveRequestController {

    @Autowired
    private IPackedObjectFactory factory;

    @PostMapping("/request_connection")
    public PackedObject requestObserveConnection(@RequestHeader("owner_user_name") String userName) throws Exception {
        ObserveConnectionManager manager=ObserveConnectionManager.getInstance();
        ObserveConnectInfo connectInfo=manager.startServer(userName);

        PackedObject packedObject=factory.getReturnValue(true,null);
        packedObject.addObject(connectInfo);
        return packedObject;
    }

}
