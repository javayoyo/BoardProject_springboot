package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
   BoardEntity boardEntity =  boardRepository.findById(commentDTO.getBoardId()).orElseThrow(() -> new NoSuchElementException());
   CommentEntity commentEntity = CommentEntity.toSaveEntity(boardEntity, commentDTO);
   return commentRepository.save(commentEntity).getId();
    }
//   ㄴ   댓글 엔티티를 리포지로 변환

    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        //1. BoardEntity 에서 댓글 목록 가져오기
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException() );
//        List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();
//        캐시 정보를 db에 반영시키도록 한다 (부모엔티티에서 자식엔티티로 접근하는경우, @Transactional 필요함

        // 2. CommentRepository 에서 가져오기
        // select * from comment_table where board_id=?
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity(boardEntity);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentEntityList.forEach(comment -> {
        commentDTOList.add(CommentDTO.toDTO(comment));
   });
        return commentDTOList;
    }
}
