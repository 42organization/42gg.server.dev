package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedUpdateRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class FeedbackAdminControllerImpl implements FeedbackAdminController{

    @Override
    @PutMapping(value = "/feedback/is-solved")
    public void feedbackIsSolvedUpdate(FeedbackIsSolvedUpdateRequestDto updateRequestDto, HttpServletRequest request){

    }
}
