package com.green.greengram.feed;


import com.green.greengram.entity.Feed;
import com.green.greengram.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//mybatis를 꼭 쓸 필요 없다. collection이나 resultMap을 안쓸거다. 그러면 아래를 쓰는게 좋다.

@Repository //없어도 자동으로 등록됨
public interface FeedRepository extends JpaRepository<Feed, Long> {  //기본적인 CRUD는 JpaRepository가 만들어줌
    //by절에 쓸 수 있는거 property명
    //
    // 적어주면 됨.
    Optional<Feed> findByFeedIdAndWriterUser(Long feedId, User writerUser);
    //쿼리메소드: findByFeedIdAndWriterUser = select 피드정보 from feed where feedId= , userId=
    //반환이 단일이면 Optional로 감싸주는게 좋음.(list는 null반환을 안함).

    //쿼리 메소드로 delete, update는 비추천. 위가 좀더 유연하다.
    int deleteByFeedIdAndWriterUser(Long feedId, User writerUser);

    //JPQL (Java Persistence Query Language)
    @Modifying //이 애노테이션이 있어야 delete or update JPQL이 가능. 리턴타입은 void or int
    @Query("delete from Feed f where f.feedId=:feedId AND f.writerUser.userId=:writerUserId") //@Query의 default는 select문
    int deleteFeed(@Param("feedId") Long feedId, @Param("writerUserId") Long writerUserId); //사실 이름 같으면 굳이 @Param 안써도 됨.
    //@Modifying했으면 int 반환 필수
    /*
        Feed(대문자로 시작) - 객체지향으로 작성해야하므로 클래스명을 작성할 것.
        writerUser.userId : 유저객체 안의 유저

        feedId=1, writerUserId=2 가정 하에 아래 SQL문이 만들어진다.
        DELETE FROM feed f
        WHERE f.feed_id= 1
        AND f.user_id= 2
    */

    /*
        만약 sql문 그대로 쓰고 싶다면?
            @Modifying
            @Query(value = "delete from feed f where f.feed_id=:feedId and f.writer_user_id=:writerUserId", nativeQuery = true)
            int deleteFeedSql(Long feedId,Long writerUserId);

        허나 유지보수성이 뛰어나서 그냥 JPQL 쓰는게 좋음. 리팩토링 리네임이 가능하다
     */


}
