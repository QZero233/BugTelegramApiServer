package com.qzero.bt.message.controller;

import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.notice.DataNotice;
import com.qzero.bt.message.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService service;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @GetMapping("/")
    public PackedObject getNotices(@AuthenticationPrincipal UserDetails userDetails){
        List<DataNotice> noticeList=service.getNoticeListByUser(userDetails.getUsername());

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("DataNoticeList",noticeList);
        return returnValue;
    }

    @DeleteMapping("/{notice_id}")
    public PackedObject deleteNotice(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable("notice_id") String noticeId) throws ResponsiveException {
        service.deleteNotice(noticeId, userDetails.getUsername());

        return packedObjectFactory.getReturnValue(true,null);
    }

}
