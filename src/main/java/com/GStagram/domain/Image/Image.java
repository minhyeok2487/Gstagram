package com.GStagram.domain.Image;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장할 수 있는 API를 제공)

import com.GStagram.domain.comment.Comment;
import com.GStagram.domain.likes.Likes;
import com.GStagram.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image { // N, 1
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략을 데이터베이스를 따라간다.
	private int id;

	private String caption;

	// 사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장
	// - DB에 그 저장된 경로를 insert
	private String postImageUrl;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({"images"})
	private User user;

	// 이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;

	// 댓글
	@OneToMany(mappedBy = "image")
	@JsonIgnoreProperties({"image"})
	@OrderBy("id DESC")
	private List<Comment> comments;

	@Transient // DB에 컬럼이 만들어지지 않는다.
	private boolean likeState;

	@Transient
	private int likeCount;
	private LocalDateTime createDate;

	@PrePersist // 디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
