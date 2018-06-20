package com.fxi.opn.controller;

/**
 * Created by seki on 18/6/12.
 */

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
        if(search.isInit()){
            userLoginService.save(search.getFpId(),search.getIp(),search.getCity());
        }

        AjaxResponseBody result = new AjaxResponseBody<MainContent>();
        logger.debug("-----"+search.getFpId() +"  "+search.getIp() + " " + search.getCity());
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        List<MainContent> contentList = postService.getPostList();
        if (contentList.isEmpty()) {
            result.setMsg("no user found!");
        } else {
            result.setMsg("0");
        }
        result.setResult(contentList);

        return ResponseEntity.ok(result);

    }

}
