package com.fxi.opn.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by seki on 18/6/21.
 */
@Entity
@Table(name = "user_topic")
public class UserTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer topicId;

    @Column
    private String fpid;

    @Column
    private Timestamp createTime;

    public UserTopic(){

    }

    public UserTopic(String fpid,Integer topicId){
        this.fpid = fpid;
        this.topicId = topicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setFpid(String fpid) {
        this.fpid = fpid;
    }

    public String getFpid() {
        return fpid;
    }
}
