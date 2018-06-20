package com.fxi.opn.service.impl;

import com.fxi.opn.dao.PostRepository;
import com.fxi.opn.dao.UserLoginRepository;
import com.fxi.opn.dao.entity.UserLogin;
import com.fxi.opn.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by seki on 18/6/20.
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginRepository userLoginRepository;
    @Override
    public void save(String fpid, String ip, String city) {
        UserLogin userLogin = new UserLogin();
        userLogin.setFpid(fpid);
        userLogin.setCity(city);
        userLogin.setIp(ip);
        userLoginRepository.save(userLogin);
    }
}
