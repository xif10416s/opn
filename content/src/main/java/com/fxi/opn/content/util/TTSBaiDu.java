package com.fxi.opn.content.util;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by seki on 18/6/19.
 */
public class TTSBaiDu {
    public static final String APP_ID = "11412955";
    public static final String API_KEY = "3C7CexGrl6KmIIhPDTNOuSho";
    public static final String SECRET_KEY = "QDS9hYSMrUzWYD7hqTeFKGmtEPsF1k8f";

    private static AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

    public static void main(String[] args) {

    }

    public static String sound2Text(){
        // 初始化一个AipSpeech

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

//        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        TtsResponse res = client.synthesis("你好百度", "zh", 1, null);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            String s = res1.toString(2);
            return s;
        } else {
            return "";
        }
    }

    /**
     * tex	String	合成的文本，使用UTF-8编码，
     请注意文本长度必须小于1024字节	是
     cuid	String	用户唯一标识，用来区分用户，
     填写机器 MAC 地址或 IMEI 码，长度为60以内	否
     spd	String	语速，取值0-9，默认为5中语速	否
     pit	String	音调，取值0-9，默认为5中语调	否
     vol	String	音量，取值0-15，默认为5中音量	否
     per	String	发音人选择, 0为女声，1为男声，
     3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女	否
     * @return
     */
    public static byte[] text2Sound(String text){
        // 设置可选参数
        if(text.getBytes().length >= 1024){
           throw  new RuntimeException("text over 1024 bytes");
        }
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5"); // 语速，取值0-9，默认为5中语速
        options.put("pit", "5"); // 音调，取值0-9，默认为5中语调
        options.put("vol", "10"); //音量，取值0-15，默认为5中音量
        options.put("per", "3"); //发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女
        TtsResponse res = client.synthesis(text, "zh", 1, options);
        JSONObject result = res.getResult();    //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        byte[] data = res.getData();            //生成的音频数据
        return data;
    }
}
