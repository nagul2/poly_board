package kr.ac.kopo.kopo20.spring.board_v3.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column
    private String title;
	
	@Column
    private String boardUserName;
	
	@Column
    private Date date;
	
	@Column(columnDefinition = "TEXT")
    private String content;
	
	@Column
    private Integer count;
	
//	CascadeType.REMOVE와의 차이: CascadeType.REMOVE는 부모 엔티티가 삭제될 때 자식 엔티티를 함께 삭제
//	orphanRemoval = true는 자식 엔티티가 부모와의 관계에서 제거될 때 해당 자식 엔티티를 삭제
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) // BoardComment와의 관계 설정
    private List<BoardComment> comments;
	
}
