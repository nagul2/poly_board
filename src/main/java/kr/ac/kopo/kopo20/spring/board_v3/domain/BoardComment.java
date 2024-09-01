package kr.ac.kopo.kopo20.spring.board_v3.domain;


import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BoardComment {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    JPA에서는 외래 키(Foreign Key)를 직접적으로 사용하는 대신, 객체 간의 연관 관계를 설정
    
    @ManyToOne(fetch = FetchType.EAGER) 				// Board와의 ManyToOne 관계 설정
    @JoinColumn(name = "board_id", nullable = false) 	// 외래키 컬럼 이름 설정
    private Board board; // Board 엔티티와 연관관계 설정

    @Column
    private String commentUserName;

    @Column
    private Date date;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private int commentLevel;
	
    @Column
    private String parentCommentUserName; // 부모 댓글의 ID
    
    @OneToMany(mappedBy = "parentCommentUserName", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoardComment> childComments; // 자식 댓글 리스트

}
