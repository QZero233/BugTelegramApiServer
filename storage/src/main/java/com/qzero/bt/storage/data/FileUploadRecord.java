package com.qzero.bt.storage.data;

import com.qzero.bt.common.view.ParameterObject;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@ParameterObject(name = "FileUploadRecord")
@Table(name = "file_upload_record")
@Entity
public class FileUploadRecord {

    public static final long MAX_TRANSPORT_SIZE=10000000;

    @Id
    @Basic
    @Column(name = "resourceId")
    private String resourceId;

    @Basic
    @Column(name = "fileLength")
    private Long fileLength;

    @Basic
    @Column(name = "blockSize")
    private Long blockSize;

    @Basic
    @Column(name = "entireBlockCount")
    private int entireBlockCount;

    @Basic
    @Column(name = "restContentSize")
    private Long restContentSize;

    @Basic
    @Column(name = "transportLastActiveTime")
    private Long transportLastActiveTime;

    @CollectionTable(name = "transported_block_index")
    @JoinColumn(name = "resourceId")
    @ElementCollection
    private List<Integer> transportedBlockIndex;

    public FileUploadRecord() {
    }

    public FileUploadRecord(String resourceId, Long fileLength, Long blockSize, int entireBlockCount, Long restContentSize, Long transportLastActiveTime, List<Integer> transportedBlockIndex) {
        this.resourceId = resourceId;
        this.fileLength = fileLength;
        this.blockSize = blockSize;
        this.entireBlockCount = entireBlockCount;
        this.restContentSize = restContentSize;
        this.transportLastActiveTime = transportLastActiveTime;
        this.transportedBlockIndex = transportedBlockIndex;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public Long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Long blockSize) {
        this.blockSize = blockSize;
    }

    public int getEntireBlockCount() {
        return entireBlockCount;
    }

    public void setEntireBlockCount(int entireBlockCount) {
        this.entireBlockCount = entireBlockCount;
    }

    public Long getRestContentSize() {
        return restContentSize;
    }

    public void setRestContentSize(Long restContentSize) {
        this.restContentSize = restContentSize;
    }

    public Long getTransportLastActiveTime() {
        return transportLastActiveTime;
    }

    public void setTransportLastActiveTime(Long transportLastActiveTime) {
        this.transportLastActiveTime = transportLastActiveTime;
    }

    public List<Integer> getTransportedBlockIndex() {
        return transportedBlockIndex;
    }

    public void setTransportedBlockIndex(List<Integer> transportedBlockIndex) {
        this.transportedBlockIndex = transportedBlockIndex;
    }

    /**
     * Give you the total count of block including incomplete block
     * @return
     */
    public int getAllBlockCount(){
        if(restContentSize==0)
            return entireBlockCount;
        return entireBlockCount+1;
    }

    @Override
    public String toString() {
        return "FileUploadRecord{" +
                "resourceId='" + resourceId + '\'' +
                ", fileLength=" + fileLength +
                ", blockSize=" + blockSize +
                ", entireBlockCount=" + entireBlockCount +
                ", restContentSize=" + restContentSize +
                ", transportLastActiveTime=" + transportLastActiveTime +
                ", transportedBlockIndex=" + transportedBlockIndex +
                '}';
    }
}
