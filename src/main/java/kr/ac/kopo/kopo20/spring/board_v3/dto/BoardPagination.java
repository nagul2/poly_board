package kr.ac.kopo.kopo20.spring.board_v3.dto;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPagination {
    private int pp;          // <<
    private int p;           // <
    private int n;           // >
    private int nn;          // >>
    private int cp;          // current page
    private int startPage;   // 시작 페이지
    private int endPage;     // 끝 페이지
    private int pageSize;    // 한 페이지에 보여줄 항목 수
    private int totalPages;  // 전체 페이지 수

    private List<BoardComment> comments = new ArrayList<>(); // 댓글 목록 추가

}
