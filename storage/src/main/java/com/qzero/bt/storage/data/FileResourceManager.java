package com.qzero.bt.storage.data;

import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.storage.service.FileUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileResourceManager {

    public static final String FILE_RESOURCE_DIR="files/storage/";
    public static final String FILE_TEMP_DIR="files/storage/temp/";

    private static String FILE_RESOURCE_ABSOLUTE_PATH=null;
    private static String FILE_TMP_ABSOLUTE_PATH=null;

    @Autowired
    private FileUploadRecordService recordService;

    static {
        new File(FILE_RESOURCE_DIR).mkdirs();
        new File(FILE_TEMP_DIR).mkdirs();

        FILE_RESOURCE_ABSOLUTE_PATH=new File(FILE_RESOURCE_DIR).getAbsolutePath();
        FILE_TMP_ABSOLUTE_PATH=new File(FILE_TEMP_DIR).getAbsolutePath();
    }


    public File getTempFile(String resourceId,int blockIndex){
        return new File(FILE_TMP_ABSOLUTE_PATH,resourceId+"-"+blockIndex+".tmp");
    }

    public File getResourceFile(String resourceId){
        return new File(FILE_RESOURCE_ABSOLUTE_PATH,resourceId);
    }

    public void mergeTempFile(String resourceId) throws IOException {
        if(!recordService.checkIfUploadFinished(resourceId))
            throw new IllegalStateException("File upload has not finished yet");

        FileUploadRecord record=recordService.getRecord(resourceId);

        RandomAccessFile randomAccessFile=new RandomAccessFile(getResourceFile(resourceId),"rw");
        randomAccessFile.setLength(record.getFileLength());

        for(int i=0;i<record.getAllBlockCount();i++){
            File file=getTempFile(resourceId,i);
            FileInputStream inputStream=new FileInputStream(file);

            byte[] buf=new byte[2048];
            int len;
            while ((len=inputStream.read(buf))!=-1){
                randomAccessFile.write(buf,0,len);
            }

            inputStream.close();
        }

        randomAccessFile.close();
    }

    public void deleteTempFiles(String resourceId){
        FileUploadRecord record=recordService.getRecord(resourceId);
        for(int i=0;i<record.getAllBlockCount();i++){
            File file=getTempFile(resourceId,i);
            if(file.exists())
                file.delete();
        }
    }

    public void deleteResourceFile(String resourceId){
        File file=getResourceFile(resourceId);
        if(file.exists())
            file.delete();
    }

}
