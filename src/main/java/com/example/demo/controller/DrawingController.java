package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Vo.featureVo;
import com.example.demo.common.File.saveFile;
import com.example.demo.common.File.retrieval;
import com.example.demo.common.utils.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.LabelMapper;
import com.example.demo.service.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-26 16:51:41
 */
@RestController
@RequestMapping("/drawing")
@CrossOrigin
public class DrawingController {
    @Autowired
    private DrawingService drawingService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private PowerService powerService;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private User1Service userService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions(":drawing:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = drawingService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("listLabel")
    public R listLabel(@RequestParam Map<String, Object> params, Integer userId) {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getDrawingPower() != 1) {
            return R.error("该用户没有权限");
        }

        PageUtils page = drawingService.queryPage(params);
        List<DrawingEntity> list = (List<DrawingEntity>) page.getList();
        QueryWrapper<LabelEntity> wrapper = new QueryWrapper<>();
        wrapper = wrapper.in("drawing_id", list.stream()
                .map(DrawingEntity::getDrawingId)
                .collect(Collectors.toList()));
        List<LabelEntity> list2 = labelService.list(wrapper);
        Map<Integer, String> labelMap = list2.stream()
                .collect(Collectors.groupingBy(LabelEntity::getDrawingId,
                        Collectors.mapping(LabelEntity::getDrawingLabel,
                                Collectors.joining(",")))
                );
        list.forEach(drawing -> {
            drawing.setDrawingLabel(labelMap.get(drawing.getDrawingId()));
        });
        powerEntity.setDrawingCount(powerEntity.getDrawingCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok().put("page", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{drawingId}")
//    @RequiresPermissions(":drawing:info")
    public R info(@PathVariable("drawingId") Integer drawingId) {
        DrawingEntity drawing = drawingService.getById(drawingId);

        return R.ok().put("drawing", drawing);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions(":drawing:save")
    public R save(@RequestBody DrawingEntity drawing) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        drawing.setCreateTime(date);
        drawingService.save(drawing);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions(":drawing:update")
    public R update(@RequestParam Integer drawingId, String drawingName, Integer userId) {
//        if (userId == null) {
//            return R.error("没有用户id");
//        }
//        PowerEntity powerEntity = powerService.selectById(userId);
//        if (powerEntity.getDrawingPower() != 1) {
//            return R.error("该用户没有权限");
//        }
        User1Entity user1 = userService.selectByUserId(userId);
        if (user1 != null && "1".equals(user1.getUserPower())) {
            DrawingEntity drawing = new DrawingEntity();
            drawing.setDrawingId(drawingId);
            drawing.setDrawingName(drawingName);
            drawingService.updateById(drawing);
            return R.ok();
        }else{
            return R.error("该用户没有权限或者没有该用户");
        }
//        powerEntity.setDrawingCount(powerEntity.getDrawingCount() + 1);
//        powerService.updateById(powerEntity);

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions(":drawing:delete")
    public R delete(@RequestParam Integer[] drawingIds, Integer userId) {
//        if (userId == null) {
//            return R.error("没有用户id");
//        }
//        PowerEntity powerEntity = powerService.selectById(userId);
//        if (powerEntity.getDrawingPower() != 1) {
//            return R.error("该用户没有权限");
//        }
        User1Entity user1 = userService.selectByUserId(userId);
        if (user1 != null && "1".equals(user1.getUserPower())) {
            drawingService.removeByIds(Arrays.asList(drawingIds));
            labelService.deleteByIds(drawingIds);
            featureService.deleteByIds(drawingIds);
            return R.ok();
//            powerEntity.setDrawingCount(powerEntity.getDrawingCount() + 1);
//            powerService.updateById(powerEntity);
        }else{
            return R.error("该用户没有权限或者没有该用户");
        }

    }

    @RequestMapping("uploadDrawing")
    public R uploadDrawing(MultipartFile file) {
        String url = null;
        try {
            url = drawingService.uploadDrawing(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(R.ok().put("url", url)).put("drawingName", file.getOriginalFilename());
    }

    @RequestMapping("selectByText")
    public R selectByText(@RequestParam String name) {
        List<DrawingEntity> drawing = drawingService.selectByText(name);

        return R.ok().put("data", drawing);
    }

    @RequestMapping("selectByLabelAndText")
    public R selectByLabel(@RequestParam(required = false, defaultValue = "") String name,
                           @RequestParam(required = false, defaultValue = "") String label,
                           Integer userId) {
        Long startTime = System.currentTimeMillis();
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getRetrievePower() != 1) {
            return R.error("该用户没有权限");
        }
        if (label != null && !label.equals("") && "".equals(name)) {
            List<LabelEntity> labelEntities = labelService.selectByLabel(label);
            List<Integer> collect = labelEntities.stream()
                    .map(LabelEntity::getDrawingId)
                    .collect(Collectors.toList());
            List<DrawingEntity> drawingEntities = drawingService.selectByIds(collect);
            List<LabelEntity> labelEntityList = labelService.selectByDrawingIds(collect);
            Map<Integer, String> labelCollect = labelEntityList.stream()
                    .collect(Collectors.groupingBy(LabelEntity::getDrawingId,
                            Collectors.mapping(LabelEntity::getDrawingLabel,
                                    Collectors.joining(",")))
                    );
            drawingEntities.forEach(
                    drawing1 -> {
                        drawing1.setDrawingLabel(labelCollect.get(drawing1.getDrawingId()));
                    }
            );
            powerEntity.setRetrieveCount(powerEntity.getRetrieveCount() + 1);
            powerService.updateById(powerEntity);
            Long endTime = System.currentTimeMillis();
            System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
            return R.ok().put("data", drawingEntities);
        } else if (name != null && !"".equals(name)) {
            List<DrawingEntity> drawing = new ArrayList<>();
            drawing = drawingService.selectByText(name);
            drawing.forEach(drawing1 -> {
                List<LabelEntity> labelEntities = labelService.selectByDrawingId(drawing1.getDrawingId());
                String collect = labelEntities.stream()
                        .map(LabelEntity::getDrawingLabel)
                        .collect(Collectors.joining(","));
                drawing1.setDrawingLabel(collect);
            });
            if ("".equals(label)) {
                powerEntity.setRetrieveCount(powerEntity.getRetrieveCount() + 1);
                powerService.updateById(powerEntity);
                Long endTime = System.currentTimeMillis();
                System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
                return R.ok().put("data", drawing);
            } else {
                List<LabelEntity> labelEntities = labelService.selectByLabel(label);
                List<Integer> labelCollect = labelEntities.stream()
                        .map(LabelEntity::getDrawingId)
                        .collect(Collectors.toList());
                List<Integer> drawingCollect = drawing.stream()
                        .map(DrawingEntity::getDrawingId)
                        .collect(Collectors.toList());
                List<DrawingEntity> finalDrawing1 = new ArrayList<>();
                if (drawingCollect.retainAll(labelCollect)) {
                    drawing.forEach(
                            drawing1 -> {
                                if (drawingCollect.contains(drawing1.getDrawingId())) {
//                                    finalDrawing.remove(drawing1);
//                                    finalDrawing1.add(drawing1);
                                    finalDrawing1.add(drawing1);
//                                    System.out.println(drawing1.toString());
                                }
                            }
                    );
                    powerEntity.setRetrieveCount(powerEntity.getRetrieveCount() + 1);
                    powerService.updateById(powerEntity);
                    Long endTime = System.currentTimeMillis();
                    System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
                    return R.ok().put("data", finalDrawing1);
                }

            }
            return R.ok();
        } else {
            return R.error("检索条件不正确");
        }

    }

    @RequestMapping("retrieve")
    public R retrieve(MultipartFile img1, Integer userId) throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getRetrievePower() != 1) {
            return R.error("该用户没有权限");
        }
        Map<String, String> map = new HashMap<>();
        map.put("url", img1.getOriginalFilename());
        byte[] imgBytes = img1.getBytes();
        HttpResponse post = HttpUtils.doPost("http://210.30.97.234:5002",
                "/student/", "post", new HashMap<>(), null, map);
        if (post.getStatusLine().getStatusCode() == 200) {
            String toString = EntityUtils.toString(post.getEntity());
            System.out.println(toString);
        }
        powerEntity.setRetrieveCount(powerEntity.getRetrieveCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok();
    }

    @RequestMapping("upload")
    public R upload(MultipartFile multipartFile, String label, Integer userId) throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getUploadPower() != 1) {
            return R.error("该用户没有权限");
        }
        saveFile.approvalFile(multipartFile);
        Date date = new Date();
        String url = "static/" + multipartFile.getOriginalFilename();
        drawingService.checkUrlUnique(url);
        drawingService.checkDrawingNameUnique(multipartFile.getOriginalFilename());
        DrawingEntity drawing = new DrawingEntity();
        drawing.setCreateTime(date);
        drawing.setDrawingUrl(url);
        drawing.setDrawingName(multipartFile.getOriginalFilename());
        drawingService.save(drawing);
        labelService.insertLabel(label, drawing.getDrawingId());
        Map<String, String> map = new HashMap<>();
        map.put(drawing.getDrawingId().toString(), url);
        map.put("label", label);
        HttpResponse post = HttpUtils.doPost("http://210.30.97.234:5002",
                "/student/", "post", new HashMap<>(), null, map);
        if (post.getStatusLine().getStatusCode() == 200) {
            String toString = EntityUtils.toString(post.getEntity());
            System.out.println(post.getStatusLine());
            FeatureEntity featureEntity = new FeatureEntity();
            featureEntity.setDrawingId(drawing.getDrawingId());
            String str = toString.replace("String", "");
            featureEntity.setDrawingGlobalFeature(str);
            featureService.save(featureEntity);
            System.out.println(toString);
        }
        powerEntity.setUploadCount(powerEntity.getUploadCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok().put("url", url);
    }

    @RequestMapping("uploads")
    public R uploads(List<MultipartFile> multipartFiles, String label, Integer userId) throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getUploadPower() != 1) {
            return R.error("该用户没有权限");
        }
        Map<String, String> map = new HashMap<>();
        map.put("label", label);
        multipartFiles
                .forEach(multipartFile -> {
                    drawingService.checkDrawingNameUnique(multipartFile.getOriginalFilename());
                    saveFile.approvalFile(multipartFile);
                    String url = "static/" + multipartFile.getOriginalFilename();
                    DrawingEntity drawing = new DrawingEntity();
                    Date date = new Date();
                    drawing.setCreateTime(date);
                    drawing.setDrawingUrl(url);
                    drawing.setDrawingName(multipartFile.getOriginalFilename());
                    drawingService.save(drawing);
                    labelService.insertLabel(label, drawing.getDrawingId());
                    map.put(drawing.getDrawingId().toString(), url);
                });

        HttpResponse post = HttpUtils.doPost("http://210.30.97.234:5000",
                "/upload", "post", new HashMap<>(), null, map);
        if (post.getStatusLine().getStatusCode() == 200) {
            String toString = EntityUtils.toString(post.getEntity());
            JSONObject jsonObject = JSON.parseObject(toString);
            System.out.println(toString);
//            map.keySet().stream().
//                    forEach(key -> {
//                        FeatureEntity featureEntity = new FeatureEntity();
//                        featureEntity.setDrawingGlobalFeature(jsonObject.getJSONObject(key).get("bigFeature").toString());
//                        featureEntity.setDrawingId(Integer.parseInt(key));
//                        featureService.save(featureEntity);
//                    });
            jsonObject.keySet().stream()
                    .forEach(key -> {
                        FeatureEntity featureEntity = new FeatureEntity();
                        featureEntity.setDrawingGlobalFeature(jsonObject.getJSONObject(key).get("bigFeature").toString());
                        featureEntity.setDrawingId(Integer.parseInt(key));
                        featureService.save(featureEntity);
                    });
        }
        powerEntity.setUploadCount(powerEntity.getUploadCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok();
    }

    @RequestMapping("uploadAll")
    public R uploadAll(@RequestParam Map<String, Object> params, Integer userId) throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getUploadPower() != 1) {
            return R.error("该用户没有权限");
        }
        List<LabelEntity> labelEntities = labelService.list();
        List<FeatureEntity> featureEntities = featureService.list();
        Map<Integer, String> labelMap = labelEntities.stream()
                .collect(Collectors.groupingBy(LabelEntity::getDrawingId,
                        Collectors.mapping(LabelEntity::getDrawingLabel,
                                Collectors.joining(",")))
                );
        List<Map<String, Object>> map = featureEntities.stream()
                .map(featureEntity -> {
                    Map<String, Object> map1 = JSON.parseObject(JSON.toJSONString(featureEntity));
                    map1.put("drawingLable", labelMap.get(featureEntity.getDrawingId()));
                    return map1;
                })
                .collect(Collectors.toList());
        Map<String, String> map1 = new HashMap<>();
        map1.put("data", JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        powerEntity.setUploadCount(powerEntity.getUploadCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok().put("map", map);
    }

    @RequestMapping("retrieval")
    public R retrieval(MultipartFile multipartFile, String label, Integer userId) throws Exception {
        if (userId == null) {
            return R.error("没有用户id");
        }
        PowerEntity powerEntity = powerService.selectById(userId);
        if (powerEntity.getRetrievePower() != 1) {
            return R.error("该用户没有权限");
        }
        String url = "static/retrieval/" + retrieval.approvalFile(multipartFile);
        Date date = new Date();
        DrawingEntity drawing = new DrawingEntity();
        drawing.setCreateTime(date);
        drawing.setDrawingUrl(url);
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("label", label);
        List<DrawingEntity> drawingEntities = new LinkedList<>();
        HttpResponse post = HttpUtils.doPost("http://210.30.97.234:5002",
                "/similar/", "post", new HashMap<>(), null, map);
        if (post.getStatusLine().getStatusCode() == 200) {
            String toString = EntityUtils.toString(post.getEntity());
            List<Integer> strList = JSON.parseArray(toString, Integer.class);
            strList.forEach(str -> {

                DrawingEntity drawing1 = drawingService.selectById(str);
                if (drawing1 != null) {
                    List<LabelEntity> labelEntities = labelService.selectByDrawingId(str);
                    String collect = labelEntities.stream()
                            .map(LabelEntity::getDrawingLabel)
                            .collect(Collectors.joining(","));
                    drawing1.setDrawingLabel(collect);
                    drawingEntities.add(drawing1);
                }

            });
        } else {
            R.error("python端没有返回");
        }
        powerEntity.setRetrieveCount(powerEntity.getRetrieveCount() + 1);
        powerService.updateById(powerEntity);
        return R.ok().put("drawingList", drawingEntities);
    }

    @RequestMapping("insert")
    public R insert() {
        drawingService.insertData();
        return R.ok();
    }

    @RequestMapping("test")
    public R test() {
        Long startTime = System.currentTimeMillis();
        drawingService.selectByText("jpg");
        Long endTime = System.currentTimeMillis();
        System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
        return R.ok();
    }

    @RequestMapping("listLabel1")
    public R listLabel(@RequestParam(value = "") List<String> strings) {
        List<DrawingEntity> drawingEntities = labelMapper.selectLabelList(strings);
        drawingEntities.forEach(drawing -> {
            System.out.println(drawing.toString());
        });
        return R.ok();
    }

    @RequestMapping("listTextAndLabel")
    public R listTextAndLabel(@RequestParam String name, @RequestParam(value = "") List<String> strings) {
        Long startTime = System.currentTimeMillis();
        List<DrawingEntity> drawingEntities = labelMapper.selectTextAndLabelList(name, strings);
        drawingEntities.forEach(
                drawing -> {
                    List<LabelEntity> labelEntities = labelService.selectByDrawingId(drawing.getDrawingId());
                    String collect1 = labelEntities.stream()
                            .map(LabelEntity::getDrawingLabel)
                            .collect(Collectors.joining(","));
                    drawing.setDrawingLabel(collect1);
                }
        );
        Long endTime = System.currentTimeMillis();
        System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
        return R.ok().put("drawing", drawingEntities);
    }

    @RequestMapping("listTextAndLabel1")
    public R listTextAndLabel1(@RequestParam String name, @RequestParam(value = "") List<String> strings) {
        Long startTime = System.currentTimeMillis();
        List<DrawingEntity> drawingEntities = labelMapper.selectTextAndLabelList(name, strings);
        Long endTime = System.currentTimeMillis();
        System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
        return R.ok().put("drawing", drawingEntities);
    }

    @RequestMapping("selectByName")
    public R selectByName(@RequestParam String name) {
        Long startTime = System.currentTimeMillis();
        List<DrawingEntity> drawingEntities = labelMapper.selectByName(name);
//        drawingEntities.forEach(drawing -> {
//            System.out.println(drawing.toString());
//        });
        Long endTime = System.currentTimeMillis();
        System.out.println("OK 耗时：" + (endTime - startTime) + "毫秒");
//        System.out.println(drawingEntities);
        return R.ok();
    }

    @RequestMapping("selectByName1")
    public R selectByName1(@RequestParam String name) {
        int[] a = new int[1];
        a[2] = 1;

        return R.ok();
    }
}
