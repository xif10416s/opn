package com.fxi.opn.dao;

import java.util.List;

import com.fxi.opn.dao.entity.Post;
import com.fxi.opn.dao.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by seki on 18/6/19.
 */

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

}
