package com.fxi.opn.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fxi.opn.dao.entity.UserTopic;

import java.util.List;

/**
 * Created by seki on 18/6/21.
 */
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {
    List<UserTopic> findByFpid(String fpid);

    void deleteByFpid(String fpid);
}
