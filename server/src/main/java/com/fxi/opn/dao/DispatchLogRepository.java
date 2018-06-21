package com.fxi.opn.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fxi.opn.dao.entity.DispatchLog;

/**
 * Created by seki on 18/6/21.
 */
public interface DispatchLogRepository extends JpaRepository<DispatchLog, Long>  {
}
