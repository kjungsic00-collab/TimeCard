package com.example.timecard.service;

import com.example.timecard.dto.ListDto;
import com.example.timecard.mapper.ListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final ListMapper listMapper;

    @Transactional
    public void uploadExcel(MultipartFile file) {
        List<ListDto> list = new ArrayList<>();

        // 1. 엑셀 파일 파싱
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ListDto dto = new ListDto();
                dto.setMemberName(getCellValueAsString(row.getCell(0)));
                dto.setCheckDate(getCellValueAsString(row.getCell(1)));
                dto.setWorkInTime(getCellValueAsString(row.getCell(2)));
                dto.setWorkOutTime(getCellValueAsString(row.getCell(3)));

                String vacation = getCellValueAsString(row.getCell(4));
                dto.setVacationYn(vacation != null && !vacation.isBlank() ? vacation : "N");

                list.add(dto);
            }
        } catch (IOException e) {
            log.error("엑셀 파일 읽기 중 입출력 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("엑셀 파일 읽기에 실패했습니다. 파일 상태를 확인해 주세요.", e);
        } catch (Exception e) {
            log.error("엑셀 파싱 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("엑셀 데이터 구조가 올바르지 않습니다.", e);
        }

        // 2. DB 저장
        try {
            for (ListDto dto : list) {
                listMapper.insert(dto); //[cite: 3]
            }
        } catch (Exception e) {
            log.error("DB 저장 중 오류 발생: {}", e.getMessage(), e);
            // RuntimeException을 던져서 @Transactional에 의한 자동 롤백 유도
            throw new RuntimeException("DB 저장 작업 중 오류가 발생했습니다.", e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}