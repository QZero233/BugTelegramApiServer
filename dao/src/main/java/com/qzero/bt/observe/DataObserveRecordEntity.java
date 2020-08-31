package com.qzero.bt.observe;

import javax.persistence.*;

@Table(name = "data_observe_record")
@Entity
public class DataObserveRecordEntity {

    @Id
    @Basic
    @Column(name = "observeId")
    private String observeId;

    @Basic
    @Column(name = "dataType")
    private int dataType;

    @Basic
    @Column(name = "dataId")
    private String dataId;

    @Basic
    @Column(name = "dataStatus")
    private int dataStatus;

    @Basic
    @Column(name = "observerUserName")
    private String observerUserName;

    @Basic
    @Column(name = "lastModifiedTime")
    private long lastModifiedTime;

    public DataObserveRecordEntity() {
    }

    public DataObserveRecordEntity(String observeId, int dataType, String dataId, int dataStatus, String observerUserName, long lastModifiedTime) {
        this.observeId = observeId;
        this.dataType = dataType;
        this.dataId = dataId;
        this.dataStatus = dataStatus;
        this.observerUserName = observerUserName;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getObserveId() {
        return observeId;
    }

    public void setObserveId(String observeId) {
        this.observeId = observeId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getObserverUserName() {
        return observerUserName;
    }

    public void setObserverUserName(String observerUserName) {
        this.observerUserName = observerUserName;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public String toString() {
        return "DataObserveEntity{" +
                "observeId='" + observeId + '\'' +
                ", dataType=" + dataType +
                ", dataId='" + dataId + '\'' +
                ", dataStatus=" + dataStatus +
                ", observerUserName='" + observerUserName + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
