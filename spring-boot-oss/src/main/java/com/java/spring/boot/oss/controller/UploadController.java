package com.java.spring.boot.oss.controller;

import com.java.spring.boot.oss.common.utils.OssUtil;
import com.java.spring.boot.oss.config.ConstantProperties;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @program: javaArchitecture
 * @description: 文件上传
 * @author: LiuYunKai
 * @create: 2020-04-14 14:50
 **/

@Controller
@RequestMapping("upload")
public class UploadController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 文件上传
     * @param file
     */
    @RequestMapping(value = "uploadBlog",method = RequestMethod.POST)
    public String uploadBlog(MultipartFile file){

        logger.info("============>文件上传");
        try {

            if(null != file){
                String filename = file.getOriginalFilename();
                if(!"".equals(filename.trim())){
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    //上传到OSS
                    String uploadUrl = OssUtil.upload(newFile);

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "index";
    }

    @RequestMapping(value = "toUploadBlog",method = RequestMethod.GET)
    public String toUploadBlog() {
        return "upload";
    }

    /**
     * 获取Object名称列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getObjectList",method = RequestMethod.GET)
    public List<String> getObjectList(){
        String bucketName = ConstantProperties.JAVA4ALL_BUCKET_NAME1;
        List<String> objectList = OssUtil.getObjectList(bucketName);
        return objectList;
    }
}
