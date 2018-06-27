package com.fxi.opn.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by seki on 18/6/15.
 */
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer topicId;

    @Column
    private Integer subTopicId;

    @Column
    private Integer sourceId;
    @Column
    private Integer mediaType;
    @Column
    private String title;
    @Column
    private String date;
    @Column
    private String author;
    @Column
    private String content;
    @Column
    private String orginUrl;
    @Column
    private String ttsUrls;

    @Column
    private Timestamp createTime;

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

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrginUrl() {
        return orginUrl;
    }

    public void setOrginUrl(String orginUrl) {
        this.orginUrl = orginUrl;
    }

    public String getTtsUrls() {
        return ttsUrls;
    }

    public void setTtsUrls(String ttsUrls) {
        this.ttsUrls = ttsUrls;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setSubTopicId(Integer subTopicId) {
        this.subTopicId = subTopicId;
    }

    public Integer getSubTopicId() {
        return subTopicId;
    }
}
