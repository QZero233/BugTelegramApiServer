package com.qzero.bt.message.service;

import com.qzero.bt.message.data.notice.DataNotice;
import com.qzero.bt.message.data.notice.DataNoticeDao;
import com.qzero.bt.message.data.notice.NoticeDataType;
import com.qzero.bt.message.notice.NoticeRemindUtils;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticeService {

    @Autowired
    private DataNoticeDao noticeDao;

    public List<DataNotice> getNoticeListByUser(String targetUserName){
        return noticeDao.findByTargetUserName(targetUserName);
    }

    public void deleteNotice(String noticeId){
        noticeDao.deleteById(noticeId);
    }

    public void addNotice(DataNotice notice){
        //Delete previous notice pointing at the same resource
        noticeDao.deleteByTargetUserNameAndDataUri(notice.getTargetUserName(),notice.getDataUri());
        noticeDao.save(notice);
    }

    public void remindTargetUser(String userName){
        NoticeRemindUtils.remindUser(userName);
    }

    public void addNotice(NoticeDataType dataType, String targetUserName, String dataId, String dataDetail){
        DataNotice notice=new DataNotice();

        String uri= UriUtils.generateUri(dataType.getTypeInString(),dataId,dataDetail);
        notice.setDataUri(uri);

        notice.setNoticeId(UUIDUtils.getRandomUUID());

        notice.setGenerateTime(System.currentTimeMillis());

        notice.setTargetUserName(targetUserName);

        addNotice(notice);
    }

    public void addNotice(NoticeDataType dataType,String targetUserName,String dataId){
        addNotice(dataType,targetUserName,dataId,null);
    }

    public void addDeleteNotice(NoticeDataType dataType,String targetUserName,String dataId){
        addNotice(dataType,targetUserName,dataId,"deleted");
    }

    public void addNoticeToGroupOfUserAndRemind(NoticeDataType dataType,List<String> targetUserNames,String dataId,String dataDetail) {
        for (String userName : targetUserNames) {
            addNotice(dataType, userName, dataId, dataDetail);
            remindTargetUser(userName);
        }
    }

}
