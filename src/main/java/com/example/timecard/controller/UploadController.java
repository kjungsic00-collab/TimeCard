package com.example.timecard.controller;

import com.example.timecard.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/up")
    public String uploadForm() {
        return "upload"; // templates/upload.html
    }

    @PostMapping("/up")
    public String upload(@RequestParam("file") MultipartFile file) {
        // UploadService를 통한 비즈니스 로직 호출
        uploadService.uploadExcel(file);

        return "redirect:/"; // 업로드 완료 후 메인 리스트로 이동
    }
}