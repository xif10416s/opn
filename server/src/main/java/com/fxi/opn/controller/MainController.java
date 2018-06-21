package com.fxi.opn.controller;

/**
 * Created by seki on 18/6/12.
 */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.fxi.opn.model.ReadLog;
import com.fxi.opn.dao.entity.UserTopic;
import com.fxi.opn.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fxi.opn.model.AjaxResponseBody;
import com.fxi.opn.model.MainContent;
import com.fxi.opn.model.SearchCriteria;
import com.fxi.opn.service.PostService;

@Controller
public class MainController {

    private static Logger logger =LoggerFactory.getLogger(MainController.class);

    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World2";

    @Autowired
    private PostService postService;

    @Autowired
    private UserLoginService userLoginService;

    List<Integer> DEFAULT_TOPIC = Arrays.asList(0,1);

    @RequestMapping("/")
    public String main() {
        logger.info("-----");
//        model.put("mainContentList", postService.getPostList());
        return "main";
    }

    @PostMapping("/main/getContentList")
    public ResponseEntity<?> getContentListViaAjax(
            @RequestBody @Valid SearchCriteria search, Errors errors) {
        logger.debug("search.isInit() "+search.isInit());
        AjaxResponseBody result = new AjaxResponseBody<MainContent>();
        if(search.isInit()){
            userLoginService.saveUserLogin(search.getFpId(),search.getIp(),search.getCity());
            List<UserTopic> userTopics = userLoginService.getUserTopics(search.getFpId());
            if(userTopics != null && userTopics.size() > 0){
                List<Integer> topicIds = userTopics.stream().map(topic -> {
                    return topic.getTopicId();
                }).collect(Collectors.toList());
                result.setTopics(topicIds);
            }
        }

        logger.debug("-----"+search.getFpId() +"  "+search.getIp() + " " + search.getCity());
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        List<Integer> topics = search.isInit() ? result.getTopics() : search.getTopics();
        if(topics == null  || topics.size() == 0){
            topics = DEFAULT_TOPIC;
        }
        List<MainContent> contentList = postService.getPostList(search.getFpId(),topics);
        if (contentList.isEmpty()) {
            result.setMsg("no user found!");
        } else {
            result.setMsg("0");
        }
        result.setResult(contentList);

        return ResponseEntity.ok(result);

    }

    @PostMapping("/main/updateTopic")
    public ResponseEntity<?> updateTopic(
            @RequestBody @Valid SearchCriteria search, Errors errors) {
        AjaxResponseBody result = new AjaxResponseBody<String>();
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        userLoginService.updateUserTopics(search.getFpId(),search.getTopics());
        result.setMsg("0");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/main/saveReadLog")
    public ResponseEntity<?> saveReadLog(
            @RequestBody @Valid ReadLog readLog, Errors errors) {
        AjaxResponseBody result = new AjaxResponseBody<String>();
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        postService.saveReadLog(readLog.getFpId(),readLog.getPostId(),readLog.getDuration(),readLog.getStartTime());
        return ResponseEntity.ok(result);
    }

}
