package com.qzero.bt.message.controller;

import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.notice.DataNotice;
import com.qzero.bt.message.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PackedObject getNotices(@RequestHeader("owner_user_name")String userName){
        List<DataNotice> noticeList=service.getNoticeListByUser(userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("DataNoticeList",noticeList);
        return returnValue;
    }

    @DeleteMapping("/{notice_id}")
    public PackedObject deleteNotice(@PathVariable("notice_id") String noticeId){
        service.deleteNotice(noticeId);

        return packedObjectFactory.getReturnValue(true,null);
    }

}
