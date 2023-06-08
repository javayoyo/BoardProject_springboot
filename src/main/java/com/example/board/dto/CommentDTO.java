package com.example.board.dto;

import com.example.board.Util.UtilClass;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private String createdAt;
    private String updatedAt;

    public static CommentDTO toDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId());
        commentDTO.setCreatedAt(UtilClass.dateFormat(commentEntity.getCreatedAt()));
        commentDTO.setUpdatedAt(UtilClass.dateFormat(commentEntity.getUpdatedAt()));

        return commentDTO;

    }
}
