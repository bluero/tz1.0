package com.example.demo.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;


public class OutputText {
    //textPath指要导出的文件的路径
    private String textPath;

    public String readText(String textname) {
        File file = new File(textPath + File.separator + textname);
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] readTextToBype(String textname) {
        File file = new File(textPath + File.separator + textname);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] inOutb = new byte[fileInputStream.available()];
            fileInputStream.read(inOutb);
            fileInputStream.close();
            return inOutb;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //textname：要导出的文件的名字；date：要导出的内容
    public boolean writeText(String textname, String date) {
        boolean flag = false;
        File filePath = new File(textPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(textPath + File.separator + textname), "UTF-8"));
            writer.write(date);
            flag = true;
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean writeByteText(String textname, byte[] date) {
        boolean flag = false;
        File filePath = new File(textPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try {
//			PrintWriter writer=new PrintWriter(new OutputStreamWriter(new FileOutputStream(textPath + File.separator + textname),"utf-8"));

            FileOutputStream fileOutputStream = new FileOutputStream(textPath + File.separator + textname);
            //Writer out = new OutputStreamWriter(fileOutputStream, "utf-8");
            fileOutputStream.write(date);
            flag = true;
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean appendText(String textName, String date) {
        boolean flag = false;
        File filePath = new File(textPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try {
            FileWriter fw = new FileWriter(
                    textPath + File.separator + textName, true);
            fw.append(date);
            flag = true;
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }
}

