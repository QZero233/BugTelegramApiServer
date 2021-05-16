package com.qzero.bt.storage.data;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@Component
public class FileResourceManager {

    public static final String FILE_RESOURCE_DIR="files/storage/";

    static {
        new File(FILE_RESOURCE_DIR).mkdirs();
    }

    public File getResourceFile(String resourceId){
        return new File(FILE_RESOURCE_DIR+resourceId);
    }

    public void deleteResourceFile(String resourceId){
        File file=getResourceFile(resourceId);
        if(file.exists())
            file.delete();
    }

    public void createResourceFile(String resourceId,long length) throws IOException {
        RandomAccessFile randomAccessFile=new RandomAccessFile(getResourceFile(resourceId),"rw");
        randomAccessFile.setLength(length);
        randomAccessFile.close();
    }

}
