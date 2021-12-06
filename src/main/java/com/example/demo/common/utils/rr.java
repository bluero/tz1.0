package com.example.demo.common.utils;


import java.io.UnsupportedEncodingException;
import java.util.Random;

public class rr {
    private static String rr(int length) {
        char[] charArray = new char[length];
        short start = (short) '0';   //0的ASCII码是48
        short end = (short) 'z';    //z的ASCII码是122（0到z之间有特殊字符）
        for (int i = 0; i < length; i++) {
            while (true) {
                char cc1 = (char) ((Math.random() * (end - start)) + start);
                if (Character.isLetterOrDigit(cc1))  //判断字符是否是数字或者字母
                {
                    charArray[i] = cc1;
                    break;
                }
            }
        }
        String StringRes = new String(charArray);//把字符数组转化为字符串
        return StringRes;
    }
    public static String getRandomChineseString(int n){
        String zh_cn = "";
        String str ="";

        // Unicode中汉字所占区域\u4e00-\u9fa5,将4e00和9fa5转为10进制
        int start = Integer.parseInt("4e00", 16);
        int end = Integer.parseInt("9fa5", 16);

        for(int ic=0;ic<n;ic++){
            // 随机值
            int code = (new Random()).nextInt(end - start + 1) + start;
            // 转字符
            str = new String(new char[] { (char) code });
            zh_cn=zh_cn+str;
        }
        return zh_cn;
    }
    public static char getRandomChar() {
        String str = "";

        int hightPos; //

        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));

        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];

        b[0] = (Integer.valueOf(hightPos)).byteValue();

        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            System.out.println("错误");

        }

        return str.charAt(0);

    }


}