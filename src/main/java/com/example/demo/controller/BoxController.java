package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.File.saveFile1;
import com.example.demo.common.utils.*;
import com.example.demo.entity.BoxEntity;
import com.example.demo.entity.LabelEntity;
import com.example.demo.entity.PowerEntity;
import com.example.demo.service.BoxService;
import com.example.demo.service.LabelService;
import com.example.demo.service.PowerService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/box")
public class BoxController {
    @Autowired
    private BoxService boxService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private PowerService powerService;


    @RequestMapping("match")
    public R match(MultipartFile multipartFile1, MultipartFile multipartFile2, Integer userId)
            throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getMatchPower() != 1) {
            return R.error("该用户没有权限");
        }
        saveFile1.approvalFile(multipartFile1);
        saveFile1.approvalFile(multipartFile2);
        Date date = new Date();
        SimpleDateFormat dFormat = new SimpleDateFormat("MM-dd-hh-ss");
        String url1 = "static/before/" + multipartFile1.getOriginalFilename();
        String url2 = "static/before/" + multipartFile2.getOriginalFilename();
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.setDrawing1Url(url1);
        boxEntity.setDrawing2Url(url2);
        boxEntity.setCreateTime(date);
        boxService.save(boxEntity);
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId.toString());
        map.put("url1", url1);
        map.put("url2", url2);
        HttpResponse post = HttpUtils.doPost("http://210.30.97.234:5001", "/similarity/",
                "post", new HashMap<>(), null, map);
        if (post.getStatusLine().getStatusCode() == 200) {
            String toString = EntityUtils.toString(post.getEntity());
            System.out.println(toString);
            JSONObject jsonObject = JSON.parseObject(toString);
            boxEntity.setDrawing1Url((String) jsonObject.get("img1"));
            boxEntity.setDrawing2Url((String) jsonObject.get("img2"));
            map.replace("url1", (String) jsonObject.get("img1"));
            map.replace("url2", (String) jsonObject.get("img2"));
            map.put("same", jsonObject.get("same").toString());
            boxService.save(boxEntity);

        }
        powerEntity.setMatchCount(powerEntity.getMatchCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok().put("map", map);
    }

    @RequestMapping("/list")
//    @RequiresPermissions(":drawing:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = boxService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("exportTxt")
    public R exportTxt() {
        List<BoxEntity> boxEntities = boxService.selectAll();
        List<String> dataList = boxEntities.stream()
                .map(boxEntity -> "time: " + boxEntity.getCreateTime() + "userid: " +
                        boxEntity.getUserId() + "drawing1: " +
                        boxEntity.getDrawing1Url() + "drawing2: " + boxEntity.getDrawing2Url())
                .collect(Collectors.toList());

        UtilFile.writeDataToTxtFile("E:\\project\\images\\txt\\abc.txt", "UTF-8",
                dataList);
        return R.ok();
    }

    @RequestMapping("updateLabel")
    public R updateLabel(Integer drawingId, String label,Integer userId) {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if(powerEntity.getDrawingPower()!=1){
            return R.error("该用户没有权限");
        }
        labelService.removeById(drawingId);
        labelService.insertLabel(label, drawingId);

        return R.ok();
    }

    @RequestMapping("selectLabel")
    public R selectLabel() {
        List<LabelEntity> labelEntities = labelService.selectAll();
        List<String> collect = labelEntities.stream()
                .map(LabelEntity::getDrawingLabel)
                .collect(Collectors.toList());
        return R.ok().put("list", collect);
    }

    @RequestMapping("db")
    public R db() throws Exception {
        String backName = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        ScheduledDataBaseBackup.dataBaseBackup("210.30.97.234",3306,"root",
                "root","tz",backName);
        return R.ok();
    }
    @RequestMapping("db1")
    public R db1() throws Exception {
        Logger log = LoggerFactory.getLogger(this.getClass());
        //数据库地址
        String url="210.30.97.234:3306";
        //用户名
        String username="root";
        //密码
        String password="root";
        //需要备份的数据库 可以多个， 多个使用逗号分隔
        String dbNames="webelectric_v2";
            String filePath = "";
            File file = new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }
//            String host = url.substring(13,27);
            List<String> strings = Arrays.asList(dbNames.split(","));
            strings.stream().forEach(dbName -> {
                try {
                    log.info("dbNme--->"+dbName);
                    //linux下保存备份的路径
                    String sqlBackPath = "backup/";
                    //保存的文件名称 DateUtility.getCurrentDate()是年月日
//                    String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
                    Date date = new Date();
                    String fileName =  date.getTime() + "_" + dbName + ".sql";
                    StringBuilder sb = new StringBuilder();
                    sb.append("mysqldump")
                            .append(" -h")
                            .append("210.30.97.234")
                            .append(" -P")
                            .append(3306)
                            .append(" -u")
                            .append("root")
                            .append(" -p'")
                            .append("root" +"'") //我的密码有特殊符号  所以使用了单引号处理
                            .append(" "+dbName)
                            .append(" > "+sqlBackPath)
                            .append(fileName);
                    log.info("sb-->"+sb.toString());
                    //linux下的命令  windows下使用 String[] command = { "cmd", "/c", sb.toString()}
                    String[] command = { "/bin/sh", "-c", sb.toString() };
                    Process process = Runtime.getRuntime().exec(command);
                    if (0 == process.waitFor()){
                        log.info("数据库备份成功,保存路径是： "+sqlBackPath);
                    }else {
                        log.info("process.waitFor()=="+process.waitFor());
                    }
                }catch (Exception e){
                    ((org.slf4j.Logger) log).error("io异常",e);
                }
            });


        return R.ok();
    }

    //dbname 要还还原的数据库名称， filepath SQL文件路径
    @RequestMapping("db2")
    public R dbRestore(){
        if("ok".equals(DbRestore.reduction("webelectric_v2","backup/1637407674770_webelectric_v2.sql"))){
            return R.ok();
        }else{
            return R.error();
        }
    }





}
