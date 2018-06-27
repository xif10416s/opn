package com.fxi;

import com.fxi.opn.content.util.QiniuTokenUtil;
import com.fxi.opn.content.util.TTSBaiDu;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by seki on 18/6/19.
 */
public class UtilTest {
    @Test
    public  void test01 (){
        byte[] bytes = TTSBaiDu.text2Sound("[观察者网综合报道]中国或参与丹麦王国自治区格陵兰岛上的机场建设，这让丹麦政府很担心：此举会让其亲密盟友美国感到不快。");
        System.out.println(bytes.length);
        String file = QiniuTokenUtil.upload(bytes,"test08.mp3");
        System.out.println(file);
    }

    @Test
    public  void test02 (){
        String[] lines = new String[] {"java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素"};
        List<String> textGroup = new ArrayList<>();
        for(String line : lines){
            if(textGroup.size() == 0){
                textGroup.add(line);
            } else {
                String keepStr =textGroup.get(textGroup.size() -1);
                String groupStr = keepStr + line;
                if(groupStr.length() > 1024){
                    textGroup.add(line);
                } else {
                    textGroup.set(textGroup.size() -1,groupStr);
                }
            }
        }
        System.out.println(textGroup.size());

    }

    @Test
    public void test03() {
        String[] lines = new String[] {"java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素","java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素java arraylist获取最后一个元素"};
        List<String> list =Arrays.asList(lines);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append("\n");
        }
        System.out.println( list.isEmpty()?"":sb.toString().substring(0, sb.toString().length() - 1));
    }
}
