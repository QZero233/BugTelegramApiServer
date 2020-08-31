package com.qzero.bt.common.observe;

import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.observe.service.ObserveService;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.observe.DataObserveRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observe")
public class ObserveController {

    @Autowired

    private IPackedObjectFactory factory;

    @Autowired
    private ObserveService service;

    @GetMapping("/records")
    public PackedObject getRecords(@RequestHeader("owner_user_name")String userName,
                                   @RequestParam("lastSyncTime")Long lastSyncTime) {

        List<DataObserveRecordEntity> records = service.getRecords(userName, lastSyncTime);
        PackedObject packedObject = factory.getReturnValue(true, null);
        packedObject.addObject("record_list", records);
        return packedObject;
    }

    @DeleteMapping("/record/{observe_id}")
    public PackedObject deleteRecord(@RequestHeader("owner_user_name")String userName,
                                    @PathVariable("observe_id") String observeId) throws ResponsiveException {
        service.deleteRecord(userName,observeId);
        return factory.getReturnValue(true,null);
    }

}
