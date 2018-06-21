package com.fxi.opn.service.impl;

import com.fxi.opn.dao.PostRepository;
import com.fxi.opn.dao.UserLoginRepository;
import com.fxi.opn.dao.UserTopicRepository;
import com.fxi.opn.dao.entity.UserLogin;
import com.fxi.opn.dao.entity.UserTopic;
import com.fxi.opn.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by seki on 18/6/20.
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserTopicRepository userTopicRepository;

    @Override
    public void save(String fpid, String ip, String city) {
        UserLogin userLogin = new UserLogin();
        userLogin.setFpid(fpid);
        userLogin.setCity(city);
        userLogin.setIp(ip);
        userLoginRepository.save(userLogin);
    }

    @Override
    public List<UserTopic> getUserTopics(String fpid) {
        return userTopicRepository.findByFpid(fpid);
    }


}
