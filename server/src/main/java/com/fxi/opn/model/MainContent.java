package com.fxi.opn.model;

/**
 * Created by seki on 18/6/15.
 */
public class MainContent {
    private String title;
    private String date;
    private String author;
    private String content;
    private String ttsUrls;
    private String orginUrl;
    private Long postId;

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

    public void setTtsUrls(String ttsUrls) {
        this.ttsUrls = ttsUrls;
    }

    public String getTtsUrls() {
        return ttsUrls;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setOrginUrl(String orginUrl) {
        this.orginUrl = orginUrl;
    }

    public String getOrginUrl() {
        return orginUrl;
    }
}
