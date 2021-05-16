package com.qzero.bt.storage.data;

import com.qzero.bt.common.view.ParameterObject;

import javax.persistence.*;

@ParameterObject(name = "FileResource")
@Table(name = "file_resources")
@Entity
public class FileResource {

    public static final int STATUS_ERROR=-1;
    public static final int STATUS_TRANSPORTING=0;
    public static final int STATUS_READY=1;
    public static final int STATUS_FREEZING=2;

    public static final long MAX_TRANSPORT_SIZE=10000000;

    @Id
    @Basic
    @Column(name = "resourceId")
    private String resourceId;

    @Basic
    @Column(name = "resourceName")
    private String resourceName;

    @Basic
    @Column(name = "resourceLength")
    private Long resourceLength;

    @Basic
    @Column(name = "resourceStatus")
    private int resourceStatus;

    @Basic
    @Column(name = "ownerUserName")
    private String ownerUserName;

    public FileResource() {
    }

    public FileResource(String resourceId, String resourceName, Long resourceLength, int resourceStatus, String ownerUserName) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceLength = resourceLength;
        this.resourceStatus = resourceStatus;
        this.ownerUserName = ownerUserName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getResourceLength() {
        return resourceLength;
    }

    public void setResourceLength(Long resourceLength) {
        this.resourceLength = resourceLength;
    }

    public int getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(int resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceLength=" + resourceLength +
                ", resourceStatus=" + resourceStatus +
                ", ownerUserName='" + ownerUserName + '\'' +
                '}';
    }
}
