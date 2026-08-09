package com.example.timecard.controller;

import com.example.timecard.dto.ListDto;
import com.example.timecard.mapper.ListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HtmlController {

    private final ListMapper listMapper;

    @GetMapping("/")
    public String indexHtml(@RequestParam(value = "startDate", required = false) String startDate,
                            @RequestParam(value = "endDate", required = false) String endDate,
                            Model model) {

        // 1. 파라미터가 비어있는 초기 접속 시: 이번 달 1일 ~ 오늘 날짜로 기본값 설정
        if (startDate == null || startDate.isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1).toString();
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        // 2. MyBatis Mapper 호출하여 데이터 조회
        List<ListDto> attendanceList = listMapper.findByPeriod(startDate, endDate);

        // 3. Model에 데이터 및 날짜 상태 전달
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "index";
    }
}