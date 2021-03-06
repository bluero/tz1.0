package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.demo.common.config.ElasticSerachConfig;
import lombok.Data;
import net.minidev.json.JSONValue;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
class Demo2ApplicationTests {
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private UserService userService;
    @Autowired
    private RestHighLevelClient client;

    private void testUpload() throws FileNotFoundException {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI4Fdn27xpGqNVUFRguaAg";
        String accessKeySecret = "HzR3qz1YgoQZNLPS4nZiU2jJkiY6LM";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("E:\\data\\work\\picture\\2019083010143670.jpg");
// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("yuanjunli", "2019083010143670.jpg", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("success");
    }
    @Test
    void contextLoads() throws FileNotFoundException {
//        User user=new User(4l,"admin","admin","admin",null,"1","2");
//        userService.deleteById(user);
//        User user=new User();
//        user.setUser_id(2l);
//        user.setCreate_time(null);
//        user.setPassword_md5("1111");
//        userMapper.insert(user);
//        User user=new User(null,"admin","admin","",null,"","");
//        userService.insertUser(user);
        System.out.println(client);
        testUpload();

    }
    @Test
    public void indexData() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
//        request.source("userName","zhangsan","age",18,"gender","m");
        User user = new User();
        user.setAge(18);
        user.setUserName("lz");
        user.setGender("m");
        String s=JSON.toJSONString(user);
//        String s=JSONValue.toJSONString(user);
//        System.out.println(s);
//        request.source(s,XContentType.JSON);
//        IndexResponse index = client.index(request, ElasticSerachConfig.COMMON_OPTIONS);
//        System.out.println(index);
    }

    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;

    }
    @Test
    public  void searchData() throws IOException {
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("users");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchQuery("userName","lz"));
//        System.out.println(searchSourceBuilder.toString());
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse search = client.search(searchRequest, ElasticSerachConfig.COMMON_OPTIONS);
//        System.out.println(search.toString());


    }

}
