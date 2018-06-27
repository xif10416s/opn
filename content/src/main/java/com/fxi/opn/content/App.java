package com.fxi.opn.content;


import java.io.File;
import java.security.MessageDigest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.misc.BASE64Encoder;

import com.fxi.opn.content.util.FileUtils;
import com.fxi.opn.content.util.QiniuTokenUtil;
import com.fxi.opn.content.util.TTSBaiDu;

/**
 * Hello world!
 *
 */
public class App {
    static String sqlTmp ="INSERT INTO post(topic_id,source_id,media_type,title,date,author,content,orgin_url,tts_urls,sub_topic_id) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?)";

    static String SOURCE_LINK = "<p><a href=\"%s\">原文链接</a></p>";
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String targetDay = sdf.format(new Date());
//        String targetDay = "20180624";
        String baseFilePath = "/Users/seki/git/projects/opn/content/crawler/opn/opn/data";
        if(args.length >=2){
            baseFilePath = args[0];
            targetDay = args[1];
        }
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(FileUtils.getConfProp("mysql_url")
                    ,FileUtils.getConfProp("mysql_user"),FileUtils.getConfProp("mysql_pass"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            doParse(baseFilePath,targetDay,conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("App finished........");
    }

    public static void doParse(String baseFilePath,String targetDay,Connection conn ) throws SQLException {
        String fileDir = baseFilePath +"/" + targetDay;
        System.out.println(fileDir);
        File baseFile = new File(fileDir);
        File[] files = baseFile.listFiles();
//        files = new File[]{files[0]};
        PreparedStatement stmt = conn.prepareStatement(sqlTmp);
        Integer count = 0;
        Integer totalCount = 0;
        for( File sourceFileDir: files){
            System.out.println(sourceFileDir.getAbsoluteFile());
            if(!sourceFileDir.isDirectory()){
                continue;
            }
            for(File file : sourceFileDir.listFiles()){
                totalCount ++;
                System.out.println(totalCount +":"+file.getAbsolutePath());
                List<String> strings = FileUtils.readFile(file);
                String[] split = strings.get(0).split(":");
                Integer sourceId = Integer.parseInt(split[0]);
                Integer topicId = Integer.parseInt(split[1]);
                Integer subTopicId = Integer.parseInt(split[2]);
                String orgUrl = strings.get(1);
                String date = strings.get(2);
                String author = strings.get(3);
                String title = file.getName();
                title = title.substring(0,title.lastIndexOf("."));
//            System.out.println(title);
                int length = listToString(strings.subList(4, strings.size()), "").length();
                if(length <= 100 || length >= 8000){
                    continue;
                }
                List<String> textGroup = new ArrayList<>();
                for(String line2 :strings.subList(4,strings.size())){
                    String pureTextLine = FileUtils.delHTMLTag(line2)
                            .replaceAll("\\s*", "").replaceAll("　","");
                    if(textGroup.size() == 0){
                        if(pureTextLine.getBytes().length > 1024){
                            Integer half = pureTextLine.length()/2;
                            textGroup.add(pureTextLine.substring(0,half));
                            textGroup.add(pureTextLine.substring(half,pureTextLine.length()));
                        } else {
                            textGroup.add(pureTextLine);
                        }
                    } else {
                        if(pureTextLine.getBytes().length > 1024){
                            Integer half = pureTextLine.length()/2;
                            textGroup.add(pureTextLine.substring(0,half));
                            textGroup.add(pureTextLine.substring(half,pureTextLine.length()));
                        } else {
                            String keepStr =textGroup.get(textGroup.size() -1);
                            String groupStr = keepStr + pureTextLine;
                            if(groupStr.getBytes().length > 1024){
                                textGroup.add(pureTextLine);
                            } else {
                                textGroup.set(textGroup.size() -1,groupStr);
                            }
                        }
                    }
                }
                try{
                    List<String> soundUrlList = new ArrayList<>();
                    String titleSoundFile = encoderByMd5(title)+".mp3";
                    QiniuTokenUtil.upload(TTSBaiDu.text2Sound(title),titleSoundFile);
                    soundUrlList.add(titleSoundFile);
                    for(int i =0 ; i<textGroup.size(); i++) {
//                System.out.println(textGroup.get(i));
                        String soundFileName = encoderByMd5(title)+i+".mp3";
                        QiniuTokenUtil.upload(TTSBaiDu.text2Sound(textGroup.get(i)),soundFileName);
                        soundUrlList.add(soundFileName);
                    }
                    stmt.setInt(1,topicId);
                    stmt.setInt(2,sourceId);
                    stmt.setInt(3,0);
                    stmt.setString(4,title);
                    stmt.setString(5,date);
                    stmt.setString(6,author);
                    strings.add(String.format(SOURCE_LINK, orgUrl));
                    stmt.setString(7,listToString(strings.subList(4,strings.size()),"\n"));
                    stmt.setString(8,orgUrl);
                    stmt.setString(9,listToString(soundUrlList,","));
                    stmt.setInt(10,subTopicId);
//            System.out.println(listToString(strings.subList(4,strings.size()),"\n").length());
                    stmt.addBatch();
                    count++;
                    if(count >= 10){
                        stmt.executeBatch();
                        count = 0;
                    }
                } catch (Exception e){
                    System.out.println("---"+file.getAbsolutePath());
                }

            }
        }
        stmt.executeBatch();
        stmt.close();
    }


    public static String encoderByMd5(String str)  {
        //确定计算方法
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newstr;
        } catch (Exception e){
            return null;
        }
    }

    public static String listToString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty()?"":sb.toString().substring(0, sb.toString().length() - 1);
    }

}
