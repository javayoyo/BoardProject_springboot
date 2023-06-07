package com.example.board.dto;

import com.example.board.entity.BoardEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString

public class BoardFileDTO {
    private Long id;
    private String originalFileName;
    private String storedFileName;

    private Long boardId;

}
