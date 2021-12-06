package com.example.demo.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.demo.common.utils.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
//@RequestMapping("/drawing")
@CrossOrigin
public class DrawingNoteController {

    @RequestMapping("logout1")
    public R logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            //设置为离线状态
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return R.ok();
    }


    @RequestMapping("oss")
    @ResponseBody
    public R policy() {
        String accessId = "LTAI4Fdn27xpGqNVUFRguaAg"; // 请填写您的AccessKeyId。
        String accessKey = "HzR3qz1YgoQZNLPS4nZiU2jJkiY6LM"; // 请填写您的AccessKeySecret。
        String endpoint = "oss-cn-shanghai.aliyuncs.com"; // 请填写您的 endpoint。
        String bucket = "yuanjunli"; // 请填写您的 bucketname 。
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//        String callbackUrl = "http://88.88.88.88:8888";
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + ""; // 用户上传文件时指定的前缀。
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        ;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
//
//            JSONObject jasonCallback = new JSONObject();
//            jasonCallback.put("callbackUrl", callbackUrl);
//            jasonCallback.put("callbackBody",
//                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=
//                    ${imageInfo.height}&width=${imageInfo.width}");
//            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
//            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
//            respMap.put("callback", base64CallbackBody);
//
//            JSONObject ja1 = JSONObject.fromObject(respMap);
//            // System.out.println(ja1.toString());
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
//            response(request, response, ja1.toString());

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return R.ok().put("data", respMap);
    }


/*
for循环
//        for(int i=0;i< multipartFiles.size();i++){
//            saveFile.approvalFile(multipartFiles.get(i));
//            url="static/"+multipartFiles.get(i).getOriginalFilename();
//            DrawingEntity drawing=new DrawingEntity();
//            Date date = new Date();
//            drawing.setCreateTime(date);
//            drawing.setDrawingUrl(url);
//            drawingService.save(drawing);
//        }
*/

/*
for 循环变量
//        for(MultipartFile file:multipartFiles){
//            saveFile.approvalFile(file);
//            url="static/"+file.getOriginalFilename();
//            DrawingEntity drawing=new DrawingEntity();
//            Date date = new Date();
//            drawing.setCreateTime(date);
//            drawing.setDrawingUrl(url);
//            drawingService.save(drawing);
//        }
*/

/*
foreach
//        multipartFiles.forEach(file->{
//            saveFile.approvalFile(file);
//            String url="static/"+file.getOriginalFilename();
//            DrawingEntity drawing=new DrawingEntity();
//            Date date = new Date();
//            drawing.setCreateTime(date);
//            drawing.setDrawingUrl(url);
//            drawingService.save(drawing);
//        });
*/

/*
    流+foreach
    multipartFiles.stream()
                .forEach(file->{
                    saveFile.approvalFile(file);
                    String url="static/"+file.getOriginalFilename();
                    DrawingEntity drawing=new DrawingEntity();
                    Date date = new Date();
                    drawing.setCreateTime(date);
                    drawing.setDrawingUrl(url);
                    drawingService.save(drawing);
                });
*/


/*
流 map+foreach
//        multipartFiles.stream()
//                .map(file->"static/"+file.getOriginalFilename())
//                .forEach(url->{
//                    DrawingEntity drawing=new DrawingEntity();
//                    Date date = new Date();
//                    drawing.setCreateTime(date);
//                    drawing.setDrawingUrl(url);
//                    drawingService.save(drawing);
//                });
*/

/*
流 map+foreach
//                .map(url->{
//                    DrawingEntity drawing=new DrawingEntity();
//                    Date date = new Date();
//                    drawing.setCreateTime(date);
//                    drawing.setDrawingUrl(url);
//                    drawingService.save(drawing);
//                    return drawing;
//                })
//                .forEach(drawing->drawingService.save(drawing));
*/


}
