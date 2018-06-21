package com.fxi.opn.dao;

import com.fxi.opn.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by seki on 18/6/19.
 */

public interface  PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findTop200ByTopicIdInOrderByDateDesc(Collection<Integer> topics);
}
