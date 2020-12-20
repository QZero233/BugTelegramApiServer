package com.qzero.bt.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.message.data.notice.DataNotice;
import com.qzero.bt.message.data.notice.DataNoticeDao;
import com.qzero.bt.message.notice.NoticeRemindUtils;
import com.qzero.bt.message.notice.action.NoticeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Async
@Service
@Transactional
public class NoticeService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DataNoticeDao noticeDao;

    public List<DataNotice> getNoticeListByUser(String targetUserName){
        return noticeDao.findByTargetUserName(targetUserName);
    }

    public void deleteNotice(String noticeId,String operatorName) throws ResponsiveException {
        if(!noticeDao.existsById(noticeId))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"Target notice does not exist");

        if(!noticeDao.existsByNoticeIdAndTargetUserName(noticeId,operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You have no access to the notice");

        noticeDao.deleteById(noticeId);
    }

    public void addNotice(String targetUserName,NoticeAction action) throws JsonProcessingException {
        String actionInString=mapper.writeValueAsString(action);
        DataNotice notice=new DataNotice();

        notice.setGenerateTime(System.currentTimeMillis());
        notice.setActionDetail(actionInString);
        notice.setNoticeId(UUIDUtils.getRandomUUID());
        notice.setTargetUserName(targetUserName);

        noticeDao.save(notice);
    }

    public void remindTargetUser(String userName){
        NoticeRemindUtils.remindUser(userName);
    }

    public void addNoticeForGroupOfUsersAndRemind(List<String> userNameList,NoticeAction action) throws JsonProcessingException {
        String actionInString=mapper.writeValueAsString(action);
        for(String userName:userNameList){
            DataNotice notice=new DataNotice();

            notice.setGenerateTime(System.currentTimeMillis());
            notice.setActionDetail(actionInString);
            notice.setNoticeId(UUIDUtils.getRandomUUID());
            notice.setTargetUserName(userName);

            noticeDao.save(notice);
            NoticeRemindUtils.remindUser(userName);
        }
    }

}
