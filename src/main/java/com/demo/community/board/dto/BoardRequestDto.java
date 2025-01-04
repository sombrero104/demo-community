package com.demo.community.board.dto;

import com.demo.community.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardRequestDto {

    @NotBlank(message = "Title cannot be empty.")
    @Size(max = 255, message = "Title must be 255 characters or less.")
    private String title;

    private String content;

    private List<Long> deleteFileIds = new ArrayList<>();

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }

}
