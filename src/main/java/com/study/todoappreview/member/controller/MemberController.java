package com.study.todoappreview.member.controller;

import com.study.todoappreview.global.aspect.TokenRequired;
import com.study.todoappreview.member.domain.request.LoginRequest;
import com.study.todoappreview.member.domain.request.SignupRequest;
import com.study.todoappreview.member.domain.response.LoginResponse;
import com.study.todoappreview.member.domain.response.MemberResponse;
import com.study.todoappreview.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return memberService.login(loginRequest);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody SignupRequest signupRequest){
        memberService.signup(signupRequest);
    }

    @GetMapping
    public Page<MemberResponse> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "3") Integer size){

        return memberService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/test")
    @TokenRequired
    public Map<String, Object> test(
            @RequestHeader("Authorization") String token
    ){
        return memberService.getTokenToData(token);
    }

}
