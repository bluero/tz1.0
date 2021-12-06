package com.example.demo.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class UtilFile {

    /**
     * @Title:writeDataToTxtFile
     * @Description:TODO(将数据写入并覆盖原文件)
     * @param destPath 目标文件路径
     * @param charset  编码格式
     * @param dataList 字符串数据集合
     * @return string
     */
    public static String writeDataToTxtFile(String filePath, String charset, List<String> dataList) {
        File txtFile = new File(filePath);// txt文件
        PrintWriter txtWriter = null;// 输出流
        FileOutputStream fo = null;
        OutputStreamWriter os = null;
        try {
            if (dataList.size() != 0) {
                // 如果文件不存在就新建文件
                if (!txtFile.exists()) {
                    txtFile.createNewFile();
                }

                // 获取流
                fo = new FileOutputStream(txtFile);
                os = new OutputStreamWriter(fo, charset);
                txtWriter = new PrintWriter(os);

                // 遍历并输出数据到文件，并防止空行
                for (int i = 0; i < dataList.size(); i++) {
                    if (i == dataList.size() - 1) {
                        txtWriter.print(dataList.get(i));
                    } else {
                        txtWriter.println(dataList.get(i));
                    }
                }

                txtWriter.flush();
                return "写入成功";
            } else {
                System.err.println("数据为空");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException异常：" + e.getCause().getMessage());
            return null;
        } finally {
            try {
                if (txtWriter != null) {
                    txtWriter.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("文件流关闭异常："+e.getCause().getMessage());
            }
        }
    }

}


