package com.atguigu.guli.service.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.service.oss.service.FileService;
import com.atguigu.guli.service.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service//业务层
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {

        //读取配置信息
        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyId = ossProperties.getKeyId();
        String keySecret = ossProperties.getKeySecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

        // 判断 bucketname 是否存在
        if (!ossClient.doesBucketExist(bucketname)) {
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }

        //构建 ObjectName avatar/2020/06/20/ddd.jpg
        String folder = new DateTime().toString("yyyy/MM/dd");
        String filename = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s1 = module + "/" + folder + "/" + filename + fileExtension;

        // 上传文件流。
        ossClient.putObject(bucketname, s1, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回 url
        //https://guli-file-han.oss-cn-beijing.aliyuncs.com/avatar/download.jpg
        return "https://" + bucketname + "." + endpoint + "/" + s1;
    }

    @Override
    public void removeFile(String url) {
        //读取配置信息
        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyId = ossProperties.getKeyId();
        String keySecret = ossProperties.getKeySecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
