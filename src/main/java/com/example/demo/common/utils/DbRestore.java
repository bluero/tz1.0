package com.example.demo.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbRestore {

    public static String reduction(String dbname,String filepath){
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("mysql")
                    .append(" -h")
                    .append("210.30.97.234")
                    .append(" -P")
                    .append(3306)
                    .append(" -u")
                    .append("root")
                    .append(" -p'")
                    .append("root" +"'")
                    .append(" "+dbname)
                    .append(" < "+filepath);

            String[] command = { "/bin/sh", "-c", sb.toString() };
            Process process = Runtime.getRuntime().exec(command);
            if (0 == process.waitFor()){

            }else {

            }
            return "ok";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
