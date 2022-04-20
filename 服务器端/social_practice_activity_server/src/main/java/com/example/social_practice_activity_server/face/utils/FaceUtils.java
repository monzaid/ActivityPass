package com.example.social_practice_activity_server.face.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName: FaceUtils
 * @Description: TODO 读取properties配置文件工具类
 * @author:
 * @createDate:
 */
public class FaceUtils {

    private static Properties property = new Properties();
    static {
        try (
                InputStream in = FaceUtils.class.getClassLoader().getResourceAsStream("face.properties");
        ) {
            property.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return property.getProperty(key);
    }

    public static Integer getInteger(String key) {
        String value = get(key);
        return null == value ? null : Integer.valueOf(value);
    }

    public static Boolean getBoolean(String key) {
        String value = get(key);
        return null == value ? null : Boolean.valueOf(value);
    }

    public static void main(String[] args) {
        System.out.println(FaceUtils.get("user"));
        System.out.println(FaceUtils.getInteger("age"));
        System.out.println(FaceUtils.getBoolean("flag"));
    }
}