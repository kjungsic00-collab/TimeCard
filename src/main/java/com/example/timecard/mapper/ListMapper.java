package com.example.timecard.mapper;

import com.example.timecard.dto.ListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ListMapper {
    List<ListDto> findByPeriod(@Param("startDate") String startDate, @Param("endDate") String endDate);

    int insert(ListDto dto);
}
