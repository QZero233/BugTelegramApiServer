package com.qzero.bt.message.data.notice;

import javax.persistence.*;

@Entity
@Table(name = "data_notice")
public class DataNotice {

    @Id
    @Basic
    @Column(name = "noticeId")
    private String noticeId;

    @Basic
    @Column(name = "targetUserName")
    private String targetUserName;

    @Basic
    @Column(name = "actionDetail")
    private String actionDetail;

    @Basic
    @Column(name = "generateTime")
    private Long generateTime;

    public DataNotice() {
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    public Long getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Long generateTime) {
        this.generateTime = generateTime;
    }

    @Override
    public String toString() {
        return "DataNotice{" +
                "noticeId='" + noticeId + '\'' +
                ", targetUserName='" + targetUserName + '\'' +
                ", actionDetail='" + actionDetail + '\'' +
                ", generateTime=" + generateTime +
                '}';
    }
}
