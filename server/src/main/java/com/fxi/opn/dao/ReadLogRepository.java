package com.fxi.opn.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fxi.opn.dao.entity.ReadLog;

import java.util.List;

/**
 * Created by seki on 18/6/21.
 */
public interface ReadLogRepository extends JpaRepository<ReadLog, Long>  {
    List<ReadLog> findByFpid(String fpId);
}
