package com.demo.community.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CommonResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String message;

    public CommonResponseDto(String message) {
        this.message = message;
        this.date = new Date();
    }

    public Date getDate() {
        return new Date();
    }

}
