package com.example.board;

import com.example.board.Util.UtilClass;
import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;


@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    @Transactional
    @DisplayName("참조관계 확인")
    public void test1() {
        BoardEntity boardEntity = boardRepository.findById(15L).get();
        // boardEntity로 첨부된 파일의 이름 조회하기(부모엔티티에서 자식엔티티를 조회하는 경우 Transactional 필요)
//        해당 사진이 있는 게시글 id 번호를 조회하여 test 진행
        System.out.println("첨부파일이름" + boardEntity.getBoardFileEntityList().get(0).getStoredFileName());

    }


    @Test
    @Transactional
    @DisplayName("엔티티 클래스 ToString")
    public void entityToString() {
        BoardEntity boardEntity = boardRepository.findById(20L).get();
        System.out.println("boardEntity = " + boardEntity);
    }
//    엔티티에서는 @ToString 을 하지 않는다 > 무한출력으로 오류

  @Test
  @Transactional
  @DisplayName("findAll 할 때 정렬해서 가져오기")
  public void findAllOrderBy() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
//       ㄴ  특정컬럼을 기준으로 내림차순으로 전체 정렬을 가져온다.
  }

  private BoardDTO newBoard(int i) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle("title" + i);
        boardDTO.setBoardWriter("writer" + i);
        boardDTO.setBoardContents("contents" + i);
        boardDTO.setBoardPass("pass" + i);
        return boardDTO;

  }
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("DB에 데이터 붓기")
    public void saveList() {
        IntStream.rangeClosed(1,50).forEach(i -> {
//            try {
//                boardService.save(newBoard(i));
                boardRepository.save(BoardEntity.toSaveEntity(newBoard(i)));
//            }catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
//        ㄴ test db 1~50까지 저장처리함

    }


    @Test
    @Transactional
    @DisplayName("페이징 객체 메서드 확인")
    public void pagingMethod() {
        int page = 22; // 요청한 페이지 번호
        int pageLimit = 3; // 한 페이지당 보여줄 글 갯수
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부
        // Page<BoardEntity> => Page<BoardDTO>
        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->

        BoardDTO.builder()
                .id(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .boardWriter(boardEntity.getBoardWriter())
                .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                .boardHits(boardEntity.getBoardHits())
                .build()
        );

//                map > 페이지를 유지하면서 데이터를 옮겨줌

        System.out.println("boardEntities.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부

    }
    
    @Test
    @DisplayName("검색 기능 테스트")
//    ㄴ 제목으로 검색
    public void searchTest() {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContaining("5");
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }

    @Test
    @Transactional
    @DisplayName("검색 기능 테스트")
//    ㄴ 작성자로 검색
    public void searchTestboardWriter() {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardWriterContaining("글");
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }

    @Test
    @Transactional
    @DisplayName("검색 기능 테스트")
//    ㄴ 제목 or 작성자로 검색
    public void searchTest1() {
        String q = "글";
        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrBoardWriterContainingOrderByIdDesc(q,q);
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }
//    String q = ""; > 전체 데이터 조회

    @Test
    @Transactional
    @DisplayName("검색결과 페이징")
    public void searchPaging() {
        String q = "2";
        int page = 0;
        int pageLimit = 3;
        Page<BoardEntity> boardEntities = boardRepository.findByBoardWriterContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->

                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardWriter(boardEntity.getBoardWriter())
                        .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                        .boardHits(boardEntity.getBoardHits())
                        .build()
        );

//                map > 페이지를 유지하면서 데이터를 옮겨줌

        System.out.println("boardEntities.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부


    }


}