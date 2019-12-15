package com.cn.gtool.controller;

import com.cn.gtool.bean.entity.SoftDO;
import com.cn.gtool.service.SoftService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(value = "/soft")
public class SoftController {
    @Resource
    private SoftService softService;


    @RequestMapping(value = "/download/{type}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void download(@PathVariable int type, HttpServletResponse response) throws FileNotFoundException {
        //获取最新的软件版本
        SoftDO softDO = this.softService.getNewSoftVersion(type);
        //根据id更新下载次数
        this.softService.updateDownNum(softDO.getId());

        String path = softDO.getPath();//"/Users/yanggang/Desktop/《码出高效：Java开发手册》.pdf";
        // 下载到本地文件的文件名
        String fileName = softDO.getFileName();//"xxxx.pdf"; // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream(path);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @RequestMapping(value = "/upload", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void upload(HttpServletRequest request, MultipartFile upfile) throws Exception {

        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = "/Users/yanggang/Desktop/temp/";//this.getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(savePath);
        //判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            file.mkdir();
        }
        // 如果没有文件上传，MultipartFile也不会为null，可以通过调用getSize()方法获取文件的大小来判断是否有上传文件
        if (upfile.getSize() > 0) {
            // 得到项目在服务器的真实根路径，如：/home/tomcat/webapp/项目名/images
//       String path = session.getServletContext().getRealPath("/");
            String path=request.getRealPath("/");
            // 得到文件的原始名称，如：美女.png
            String fileName = upfile.getOriginalFilename();
            // 通过文件的原始名称，可以对上传文件类型做限制，如：只能上传jpg和png的图片文件
            if (fileName.endsWith("pdf") || fileName.endsWith("png") || fileName.endsWith("txt")) {
                File file1 = new File(savePath, fileName);
                upfile.transferTo(file1);
//                return "/success.jsp";
            }
        }
//        return "/error.jsp";
    }

}
