package com.example.demo.common.File;



import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class saveFile1 {
    // An highlighted block
    public static void approvalFile(MultipartFile filecontent) {
        OutputStream os = null;
        InputStream inputStream = null;
        String fileName = null;
        Date date = new Date();
        SimpleDateFormat dFormat = new SimpleDateFormat("MM-dd-hh-ss");
        try {
            inputStream = filecontent.getInputStream();
            String[] str=filecontent.getOriginalFilename().split("\\.");
            for (int i=0;i<str.length;i++){
                System.out.println(str[i]);
            }
            fileName = dFormat.format(date)+"."+str[1];
//            System.out.println(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            String path = "C:\\Users\\Administrator\\Downloads\\nginx-1.16.1\\html\\static\\before";
            String path="/usr/share/nginx/html/static/before";

            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
//            File.separator+filecontent.getOriginalFilename()
            os = new FileOutputStream(tempFile.getPath() +File.separator +filecontent.getOriginalFilename());
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
