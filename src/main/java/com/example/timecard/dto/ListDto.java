package com.example.timecard.dto;

import lombok.*;

@Data
public class ListDto {
    private String id;
    private String memberName;
    private String checkDate;
    private String workInTime;   // 출근 시간
    private String workOutTime;  // 퇴근 시간
    private String vacationYn;
}
