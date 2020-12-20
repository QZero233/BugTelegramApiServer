package com.qzero.bt.message.notice;

import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@Transactional
public class NoticeRequestController {

    @Autowired
    private IPackedObjectFactory factory;

    @GetMapping("/request_connection")
    public PackedObject requestObserveConnection(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        NoticeConnectionManager manager= NoticeConnectionManager.getInstance();
        NoticeConnectInfo connectInfo=manager.startServer(userDetails.getUsername());

        PackedObject packedObject=factory.getReturnValue(true,null);
        packedObject.addObject(connectInfo);
        return packedObject;
    }

}
