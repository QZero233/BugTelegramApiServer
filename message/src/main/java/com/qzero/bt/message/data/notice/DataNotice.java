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
    @Column(name = "dataUri")
    private String dataUri;

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

    public String getDataUri() {
        return dataUri;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
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
                ", dataUri='" + dataUri + '\'' +
                ", generateTime=" + generateTime +
                '}';
    }
}
