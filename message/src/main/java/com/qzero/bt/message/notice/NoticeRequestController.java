package com.qzero.bt.message.notice;

import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@Transactional
public class NoticeRequestController {

    @Autowired
    private IPackedObjectFactory factory;

    @GetMapping("/request_connection")
    public PackedObject requestObserveConnection(@RequestHeader("owner_user_name") String userName) throws Exception {
        NoticeConnectionManager manager= NoticeConnectionManager.getInstance();
        NoticeConnectInfo connectInfo=manager.startServer(userName);

        PackedObject packedObject=factory.getReturnValue(true,null);
        packedObject.addObject(connectInfo);
        return packedObject;
    }

}
