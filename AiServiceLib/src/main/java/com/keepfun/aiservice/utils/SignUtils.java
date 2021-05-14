package com.keepfun.aiservice.utils;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.aiservice.utils.base64.Base64;

import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @description
 * @date 2020/9/1 7:38 PM
 */
public class SignUtils {

    /**
     * 数字签名，密钥算法
     */
    private static final String RSA_KEY_ALGORITHM = "RSA";

    /**
     * 数字签名签名/验证算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
//    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
//    private static final String SIGNATURE_ALGORITHM = "RSA/ECB/PKCS1Padding";


    /**
     * 生成签名
     *
     * @param object 加密内容
     * @param priKey 私钥
     */
    public static String signGen(Object object, String priKey) {
        if (object == null) {
            return null;
        }
        LogUtils.e("getSignContent object : " + object);
        return sign(getSignContent(object), priKey);
    }

    /**
     * RSA签名 - 私钥进行签名
     *
     * @param data   待签名数据
     * @param priKey 私钥
     * @return 签名
     */
    public static String sign(String data, String priKey) {
        try {
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);

            // 生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // 实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

            // 初始化Signature
            signature.initSign(privateKey);

            // 更新
            signature.update(data.getBytes());

            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            LogUtils.e("RSA数字签名异常，", e);
            return null;
        }
    }

    /**
     * RSA签名
     *
     * @param data   待签名数据
     * @param priKey 私钥
     * @return 签名
     */
    public static String sign(byte[] data, byte[] priKey) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        // 初始化Signature
        signature.initSign(privateKey);
        // 更新
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }



    /**
     * 对参数进行筛选过滤、排序(ASCII码递增排序) 组合成“参数=参数值”的格式，并且把这些参数用&字符连接起来
     *
     * @param object 参数对象
     * @return
     */
    public static String getSignContent(Object object) {
        Map<String, Object> params = objectToMap(object);
        return getSignContent(params);
    }

    public static String getSignContent(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!"sign".equals(key) && !"serialVersionUID".equals(key)) {
                String value = String.valueOf(params.get(key));
                if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                    content.append((index == 0) ? "" : "&").append(key).append("=").append(value);
                    index++;
                }
            }
        }
        LogUtils.e("getSignContent content : " + content );
        return content.toString();
    }


    /**
     * Object转Map
     */
    private static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value == null) {
                    value = "";
                }
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            LogUtils.e("objectToMap 异常，", e);
        }

        return map;
    }

}