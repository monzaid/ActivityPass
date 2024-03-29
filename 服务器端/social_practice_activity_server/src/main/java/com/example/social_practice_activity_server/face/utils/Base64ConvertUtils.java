package com.example.social_practice_activity_server.face.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @author:
 * @description: TODO 图片和Base64字符串转换
 * @date:
 * @version:
 */
public class Base64ConvertUtils {

    /**
     * @Description： 图片转化成base64字符串
     * @param: path 路径
     * @Return:
     */
    public static String getImageStr(String path) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //待处理的图片
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * @Description： 图片转化成base64字符串
     * @param: file 类型
     * @Return:
     */
    public static String getImageStr(MultipartFile file) {
        //待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = file.getInputStream();
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }


    /**
     * @Description： base64字符串转化成图片
     * @param: imgStr
     * @Return:
     */
    public static boolean GenerateImage(String imgStr, String photoname) {
        //对字节数组字符串进行Base64解码并生成图片
        //图像数据为空
        if (imgStr == null) {
            return false;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    //调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imagePath = "E:/upload/img";
            //System.currentTimeMillis()
            //新生成的图片
            String imgFilePath = imagePath + photoname;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
