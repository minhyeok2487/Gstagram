package com.GStagram.domain.subscribe;


import com.GStagram.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 어노테이션이 없어도 JpaRepository를 상속하면 IoC 등록이 자동으로 된다.
public interface SubscribeRepository extends JpaRepository<Subscribe,Integer> {

	@Modifying // INSERT, DELETE, UPDATE를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요
	@Query(value = "INSERT INTO subscribe(FROM_USER_ID, TO_USER_ID, CREATE_DATE) VALUES (:fromUserId, :toUserId, now())"
	, nativeQuery = true)
	void mSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId); // 1 (변경된 행의 개수가 리턴됨), 실패하면 -1 리턴

	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE FROM_USER_ID = :fromUserId AND TO_USER_ID = :toUserId"
		, nativeQuery = true)
	void mUnSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE FROM_USER_ID = :principalId AND TO_USER_ID =:pageUserId",nativeQuery = true)
	int mSubscribeState(@Param("principalId") int principalId, @Param("pageUserId") int pageUserId);

	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE FROM_USER_ID = :pageUserId",nativeQuery = true)
	int mSubscribeCount(@Param("pageUserId") int pageUserId);
}
