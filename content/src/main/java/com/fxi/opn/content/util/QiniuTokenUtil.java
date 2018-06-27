package com.fxi.opn.content.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.UrlSafeBase64;

/**
 * Created by seki on 18/6/19.
 */
public class QiniuTokenUtil {
    public static final String BASE_URL = "http://pajw3brke.bkt.clouddn.com/";

    public static String generateUploadToken() {
        // 1 构造上传策略
        try {
            JSONObject _json = new JSONObject();
            long _dataline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _dataline);// 有效时间为 5 分钟
            _json.put("scope", "opnw");
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign;
            _sign = HmacSHA1Encrypt(_encodedPutPolicy,
                    FileUtils.getConfProp("QiniuSecretKey"));
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = FileUtils.getConfProp("QiniuAccessKey") + ':'
                    + _encodedSign + ':' + _encodedPutPolicy;
            return _uploadToken;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /**
     *
     *
     * @param encryptText
     *            被签名的字符串
     * @param encryptKey
     *            密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    public static void main(String[] args) {
        System.out.println(generateUploadToken());
        String upload = upload(getBytes("/Users/seki/git/projects/opn/content/src/main/resources/conf.properties"), "log_followvideo.txt" + System.currentTimeMillis());
        System.out.println(upload);
    }

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    static UploadManager uploadManager = new UploadManager();

    public static String upload(byte[] fileBytes, String key) {
        try {
            Response res = uploadManager.put(fileBytes, key, generateUploadToken());
            MyRet ret = res.jsonToObject(MyRet.class);
            return ret.key;
        } catch (QiniuException e) {
            Response r = e.response;
        }

        return null;
    }

    public class MyRet {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }
}
