package com.fxi.opn.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fxi.opn.dao.entity.Post;

/**
 * Created by seki on 18/6/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void test01(){
        ArrayList<Integer> integers = new ArrayList<>();
//        integers.add(1);
//        integers.add(0);
        List<Post> top10ByOrderByDate = postRepository.findTop200ByTopicIdInOrderByDateDesc(integers);
        for (Post p :top10ByOrderByDate) {
            System.out.println(p.getTopicId() +" : " +p.getTitle());
        }
    }
}
