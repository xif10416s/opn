package com.fxi.opn.service;

import java.util.List;
import java.util.Set;

import com.fxi.opn.model.MainContent;

/**
 * Created by seki on 18/6/19.
 */
public interface PostService {
    List<MainContent> getPostList(String fpId,List<Integer> topics);

    void saveReadLog(String fpId,Long postId,Integer duration,Long startTime);

    Set<Long> getReadLog(String fpId);
}
