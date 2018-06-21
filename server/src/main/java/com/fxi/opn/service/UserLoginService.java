package com.fxi.opn.service;

import com.fxi.opn.dao.entity.UserTopic;

import java.util.List;

/**
 * Created by seki on 18/6/20.
 */
public interface UserLoginService {
    void saveUserLogin(String fpid, String ip, String city);

    List<UserTopic> getUserTopics(String fpid);

    void deleteUserTopicByFpid(String fpid);

    void updateUserTopics(String fpid, List<Integer> topics);
}
