package com.xh.sys.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.xdevapi.JsonArray;
import com.xh.common.vo.Result;
import com.xh.sys.entity.Files;
import com.xh.sys.mapper.FilesMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-13
 */
@Api(tags = {"文件接口"})  //Swagger文档注解
@RestController
@RequestMapping("/files")
public class FilesController {
    @Resource
    private FilesMapper filesMapper;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  //文件名
        String type = FileUtil.extName(originalFilename);  //文件类型
        long size = file.getSize();  //文件大小

        //文件路径
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;  //定义一个唯一的文件标识码
        File uploadFile = new File(fileUploadPath + fileUUID);

        //判断文件目录是否存在
        if(!uploadFile.getParentFile().exists()) uploadFile.mkdirs();

        //先上传文件至磁盘
        file.transferTo(uploadFile);
        //获取文件的md5
        String  md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否有相同的记录
        Files dbFiles = getFilesByMd5(md5);
        String url; //数据库中存储的url
        if(dbFiles!=null){
            //存在重复文件则不上传至磁盘，即删除原记录
            url = dbFiles.getUrl();
            uploadFile.delete();
        }else{
            //不存在重复文件则上传至磁盘，即不删除
            url = "http://" + serverIp +":9999/files/" + fileUUID;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);//单位KB
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);
        return url;
    }

    //查询文件的md5是否已存在
    private Files getFilesByMd5(String md5) {
        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.eq("md5",md5);
        List<Files> filesList = filesMapper.selectList(wrapper);
        return filesList.size()==0?null:filesList.get(0);
    }

    @ApiOperation("富文本上传文件")
    @PostMapping("/uploadImg")
    public Object uploadImg(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  //文件名
        String type = FileUtil.extName(originalFilename);  //文件类型
        long size = file.getSize();  //文件大小

        //文件路径
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;  //定义一个唯一的文件标识码
        File uploadFile = new File(fileUploadPath + fileUUID);

        //判断文件目录是否存在
        if(!uploadFile.getParentFile().exists()) uploadFile.mkdirs();

        //先上传文件至磁盘
        file.transferTo(uploadFile);
        //获取文件的md5
        String  md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否有相同的记录
        Files dbFiles = getFilesByMd5(md5);
        String url; //数据库中存储的url
        if(dbFiles!=null){
            //存在重复文件则不上传至磁盘，即删除原记录
            url = dbFiles.getUrl();
            uploadFile.delete();
        }else{
            //不存在重复文件则上传至磁盘，即不删除
            url = "http://" + serverIp +":9999/files/" + fileUUID;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);//单位KB
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);

//        {
//            // errno 即错误代码，0 表示没有错误。
//            //       如果有错误，errno != 0，可通过下文中的监听函数 fail 拿到该错误码进行自定义处理
//            "errno": 0,
//
//                // data 是一个数组，返回图片Object，Object中包含需要包含url、alt和href三个属性,它们分别代表图片地址、图片文字说明和跳转链接,alt和href属性是可选的，可以不设置或设置为空字符串,需要注意的是url是一定要填的。
//                "data": [
//            {
//                url: "图片地址",
//                        alt: "图片文字说明",
//                    href: "跳转链接"
//            },
//            {
//                url: "图片地址1",
//                        alt: "图片文字说明1",
//                    href: "跳转链接1"
//            },
//            "……"
//    ]
//        }
        return JSONUtil.parseObj("{\"errno\":0, \"data\":[{\"url\": \""+url+"\"}]}");
    }

    @ApiOperation("下载文件")
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException{
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath+fileUUID);
        //设置输出流的格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }

    @ApiOperation("根据文件id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<?> deleteFilesById(@PathVariable("id") Integer filesId){
        filesMapper.deleteById(filesId);
        return Result.success("删除文件成功");
    }
}


