package com.ustlearn.controller;

import com.ustlearn.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    //文件上传必须声明参数multipartFile 为springWeb框架封装类 名字必须跟前端的FormData中name保持一致
    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
        //file是一个临时文件,需要转存到指定位置,否则本次请求完成后文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取后缀拼接到uuid生成的名字后面
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名,防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //yml文件中配置了存储路径,判断是否有文件夹,没有则创建 file可表示文件也可表示文件夹
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在,需要创建
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回文件名称,前端存储图片将其保存到数据库里.
        return Result.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    //下载不需要返回值,通过流的方式写回
    //输出流需要response获得,现在是向浏览器写回数据,通过response响应对象来get一个流
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //输入流,通过输入流读取文件内容
            FileInputStream fis = new FileInputStream(new File(basePath + name));

            //输出流,通过输出流将文件写回浏览器,在浏览器展示图片

            //固定的,设置响应回去的文件为图片文件
            response.setContentType("image/jpeg");

            ServletOutputStream sos = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            //字节流,fis一直读取放到bytes中,直到读完.
            //每次循环度1024个byte.
            while ((len = fis.read(bytes)) != -1) {
                sos.write(bytes, 0, len);
                sos.flush();
            }
            sos.close();
            fis.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
