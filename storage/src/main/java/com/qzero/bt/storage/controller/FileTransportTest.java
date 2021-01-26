package com.qzero.bt.storage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/file")
public class FileTransportTest {

    @ResponseBody
    @PostMapping("/upload_test")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception{
        file.transferTo(new File("H:\\1.txt"));

        return file.getOriginalFilename();
    }

    @GetMapping("/download_test")
    public void download(HttpServletResponse response) throws Exception{
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=1.txt");

        byte[] buff = new byte[1024];
        //创建缓冲输入流
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("H:\\1.txt")));;
        OutputStream outputStream = response.getOutputStream();

        int read = bis.read(buff);
        while (read != -1) {
            outputStream.write(buff, 0, read);
            outputStream.flush();
            read = bis.read(buff);
        }

        bis.close();
        outputStream.close();
    }

}
