package com.fxi.opn.service.impl;

import com.fxi.opn.dao.PostRepository;
import com.fxi.opn.dao.entity.Post;
import com.fxi.opn.model.MainContent;
import com.fxi.opn.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seki on 18/6/19.
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<MainContent> getPostList(String fpid ,List<Integer> topics) {
        List<Post> top10ByOrderByDate = postRepository.findTop200ByTopicIdInOrderByDateDesc(topics);
        List<MainContent> rsList = new ArrayList<MainContent>();
        for(Post p : top10ByOrderByDate){
            MainContent mainContent = new MainContent();
            mainContent.setTitle(p.getTitle());
            mainContent.setDate(p.getDate());
            mainContent.setAuthor(p.getAuthor());
            mainContent.setContent(p.getContent());
            mainContent.setTtsUrls(p.getTtsUrls());
            rsList.add(mainContent);
        }
        return rsList;
    }
}
