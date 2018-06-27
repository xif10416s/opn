package com.fxi.opn.service.impl;

import com.fxi.opn.dao.PostRepository;
import com.fxi.opn.dao.ReadLogRepository;
import com.fxi.opn.dao.entity.Post;
import com.fxi.opn.dao.entity.ReadLog;
import com.fxi.opn.model.MainContent;
import com.fxi.opn.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by seki on 18/6/19.
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReadLogRepository readLogRepository;

    public static Integer BATCH_SIZE = 10;

    public static Integer GET_PAGE_SIZE = 500;

    @Override
    public List<MainContent> getPostList(String fpid ,List<Integer> subTopics) {
        Integer fromPage = 0;
        Pageable top =  PageRequest.of(fromPage, GET_PAGE_SIZE);
        List<Post> onePage = null;
        if(subTopics == null || subTopics.size() ==0){
            onePage = postRepository.findByOrderByDateDesc(top);
        } else {
            onePage = postRepository.findBySubTopicIdInOrderByDateDesc(subTopics,top);
        }
        Set<Long> readLogSet = getReadLog(fpid);
        List<Post> filterPost = onePage.stream().filter(post -> {
            return !readLogSet.contains(post.getId());
        }).collect(Collectors.toList());
        List<MainContent> rsList = new ArrayList<MainContent>();
        if(filterPost.size() >= BATCH_SIZE){
            for(Post p : filterPost.subList(0,BATCH_SIZE)){
                MainContent mainContent = new MainContent();
                mainContent.setTitle(p.getTitle());
                mainContent.setDate(p.getDate());
                mainContent.setAuthor(p.getAuthor());
                mainContent.setContent(p.getContent());
                mainContent.setTtsUrls(p.getTtsUrls());
                mainContent.setPostId(p.getId());
//                mainContent.setOrginUrl(p.getOrginUrl());
                rsList.add(mainContent);
            }
        } else {
            //TODO
        }

        return rsList;
    }

    @Override
    public void saveReadLog(String fpId, Long postId, Integer duration, Long startTime) {
        ReadLog readLog = new ReadLog();
        readLog.setFpid(fpId);
        readLog.setPostId(postId);
        readLog.setDuration(duration);
        readLog.setStartTime(new Timestamp(startTime));
        readLogRepository.save(readLog);
    }

    @Override
    public Set<Long> getReadLog(String fpId) {
        List<ReadLog> readLogs = readLogRepository.findByFpid(fpId);
        HashSet<Long> posts = new HashSet<>();
        if(readLogs!= null && readLogs.size() >0){
            for(ReadLog readLog : readLogs){
                posts.add(readLog.getPostId());
            }
        }
        return posts;
    }
}
