package com.example.board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")

public class BoardFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String originalFileName;

    @Column(length = 100)
    private String storedFileName;

    @ManyToOne
    @JoinColumn(name = "board_id") // 실제 DB에 생성되는 참조 컬럼의 이름
    private BoardEntity boardEntity; // 부모 엔티티 타입으로 정의

    public static BoardFileEntity toSaveBoardFileEntity(BoardEntity savedEntity, String originalFileName, String storedFileName) {
    BoardFileEntity boardFileEntity = new BoardFileEntity();
    boardFileEntity.setBoardEntity(savedEntity);
    boardFileEntity.setOriginalFileName(originalFileName);
    boardFileEntity.setStoredFileName(storedFileName);
    return boardFileEntity;
    }
}
