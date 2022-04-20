package com.example.social_practice_activity.Utils;

import com.alibaba.fastjson.JSON;

//import org.json.JSONObject;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebUtils {

    public static <T> Map<String, Object> Object2Map(T object){
        return JSONObject.parseObject(JSON.toJSONString(object));
    }

//    /**
//     * 把Map中的值注入到对应的JavaBean属性中。
//     * @param value
//     * @param bean
//     */
//    public static <T> T copyParamToBean(Map value , T bean ){
//        try {
//            System.out.println("注入之前：" + bean);
//            /**
//             * 把所有请求的参数都注入到user对象中
//             */
//            return JSON.parseObject(json, bean.class);
//            BeanUtils.populate(bean, value);
//            System.out.println("注入之后：" + bean);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }

    /**
     * 将字符串转换成为int类型的数据
     * @param strInt
     * @param defaultValue
     * @return
     */
    public static int parseInt(String strInt,int defaultValue) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 将字符串转换成为double类型的数据
     * @param strDouble
     * @param defaultValue
     * @return
     */
    public static double parseDouble(String strDouble,double defaultValue) {
        try {
            return Double.parseDouble(strDouble);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return defaultValue;
    }
}
