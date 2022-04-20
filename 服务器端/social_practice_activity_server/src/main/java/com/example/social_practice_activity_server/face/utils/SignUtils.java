package com.example.social_practice_activity_server.face.utils;

import net.sf.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * @author:
 * @description: TODO 腾讯云 签名方法
 * @date:
 * @version:
 */

public class SignUtils {

    private final static String CHARSET = "UTF-8";

    private final static Charset UTF8 = StandardCharsets.UTF_8;

    public static String sign(TreeMap<String, Object> params, HttpMethodEnum menth, SignMenodEnum signMenodEnum,
                              String jsonString, String reqUrl, String sercretKey, ContentTypeEnum typeEnum) throws Exception {
        String signString = null;
        String secretId = String.valueOf(params.get("SecretId"));

        switch (signMenodEnum) {
            case TC3_HMAC_SHA256:
                String replace = reqUrl.replace("https://", "");
                String service = replace.substring(0, 3);
                String host = replace;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 注意时区，否则容易出错
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String date = sdf.format(new Date(Long.valueOf(params.get("Timestamp") + "000")));
                // ************* 步骤 1：拼接规范请求串 *************
                String canonicalUri = "/";
                String canonicalQueryString = "";
                String canonicalHeaders = "content-type:" + typeEnum.getName() + "\n" + "host:" + host + "\n";
                String signedHeaders = "content-type;host";

                String hashedRequestPayload = sha256Hex(jsonString);
                String canonicalRequest = menth.getName() + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                        + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

                // ************* 步骤 2：拼接待签名字符串 *************
                String credentialScope = date + "/" + service + "/" + "tc3_request";
                String hashedCanonicalRequest = sha256Hex(canonicalRequest);
                String stringToSign = signMenodEnum.getMendoName() + "\n" + params.get("Timestamp") + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
                System.out.println("待签名参数:{}" + stringToSign);

                // ************* 步骤 3：计算签名 *************
                byte[] secretDate = hmac256(("TC3" + sercretKey).getBytes(UTF8), date);
                byte[] secretService = hmac256(secretDate, service);
                byte[] secretSigning = hmac256(secretService, "tc3_request");
                String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
                System.out.println("生成签名参数:{}" + signature);

                // ************* 步骤 4：拼接 Authorization *************
                String authorization = signMenodEnum.getMendoName() + " " + "Credential=" + secretId + "/" + credentialScope + ", "
                        + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
                System.out.println("生成authorization参数:{}" + authorization);

                JSONObject headers = new JSONObject();
                headers.put("Authorization", authorization);
                headers.put("Content-Type", typeEnum.getName());
                headers.put("Host", host);
                headers.put("X-TC-Action", String.valueOf(params.get("Action")));
                headers.put("X-TC-Timestamp", String.valueOf(params.get("Timestamp")));
                headers.put("X-TC-Version", String.valueOf(params.get("Version")));
                if (Objects.nonNull(params.get("Region"))) {
                    headers.put("X-TC-Region", String.valueOf(params.get("Region")));
                }
                signString = headers.toString();

                break;
            default:
                StringBuilder s2s = new StringBuilder(reqUrl.replace("https://", menth.getName()) + "/?");
                // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
                for (String k : params.keySet()) {
                    s2s.append(k).append("=").append(params.get(k).toString()).append("&");
                }
                String s = s2s.toString().substring(0, s2s.length() - 1);
                Mac mac = Mac.getInstance(signMenodEnum.getMendoName());
                SecretKeySpec secretKeySpec = new SecretKeySpec(sercretKey.getBytes(CHARSET), mac.getAlgorithm());
                mac.init(secretKeySpec);
                byte[] hash = mac.doFinal(s.getBytes(CHARSET));
                signString = DatatypeConverter.printBase64Binary(hash);
                break;
        }
        return signString;
    }

    /**
     * 获取签名之后的请求Url
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getUrl(TreeMap<String, Object> params, String reqUrl) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(reqUrl + "/?");
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }

    public static String getUrl(TreeMap<String, Object> params, String reqUrl, String jsonString, ContentTypeEnum typeEnum) {
        String replace = reqUrl.replace("https://", "");
        StringBuilder sb = new StringBuilder();
        sb.append("curl -X POST https://").append(replace)
                .append(" -H \"Authorization: ").append(params.get("Authorization")).append("\"")
                .append(" -H \"Content-Type:").append(typeEnum.getName())
                .append(" -H \"Host: ").append(replace).append("\"")
                .append(" -H \"X-TC-Action: ").append(params.get("Action")).append("\"")
                .append(" -H \"X-TC-Timestamp: ").append(params.get("Timestamp")).append("\"")
                .append(" -H \"X-TC-Version: ").append(params.get("Version")).append("\"");
        if (Objects.nonNull(params.get("Region"))) {
            sb.append(" -H \"X-TC-Region: ").append(params.get("Region")).append("\"");
        }
        sb.append(" -d '").append(jsonString).append("'");
        return sb.toString();
    }


    public static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(UTF8));
    }


    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(UTF8));
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }
}