package com.ll.mb.domain.home.home.controller;

import com.ll.mb.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showMain() {
        // 상품 목록 페이지로 리다이렉트
        return "redirect:/product/list";
    }
}