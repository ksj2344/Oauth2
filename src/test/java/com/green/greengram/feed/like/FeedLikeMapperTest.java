package com.green.greengram.feed.like;

import com.green.greengram.feed.like.model.FeedLikeReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
//import static: .Assertions 아래의 static매서드들은 class명 안쓰고 써도 됨.


@ActiveProfiles("test") //yaml 적용되는 파일 선택(application-test.yml)
@MybatisTest //Mybatis Mapper Test이기 때문에 작성 >> Mapper 들이 전부 객체화 >> DI를 할 수 있다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//테스트는 기본적으로 메모리 데이터베이스(H2)를 사용하여 하는데, 메모리 데이터 베이스로 교체하지 않겠다, (AutoConfigureTestDatabase.Replace.NONE)
    // 즉, 우리가 원래 쓰는 데이터베이스로 테스트를 진행하겠다.

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 이테스트를 실행할 때마다 클래스(PER_CLASS)단위로 객체화 하겠다. 기본값음 메서드단위
class FeedLikeMapperTest {
    //테스트는 생성자를 이용한 DI가 불가능
    //DI방법은 필드, setter메소드, 생성자
    //테스트 때는 필드 주입방식을 활용함. (필드 주입 방식일 때는 private 필드에도 주입이 가능)

    @Autowired //스프링 컨테이너가 DI 해주는 에노테이션.
    private FeedLikeMapper feedLikeMapper; //필드 주입 방식의 DI
    static final long FEED_ID_1=1L;
    static final long FEED_ID_5=5L;
    static final long USER_ID_2=2L;

    static final FeedLikeReq existedData=new FeedLikeReq();
    static final FeedLikeReq notExistedData=new FeedLikeReq();

    /*
        @BeforeAll : 모든 테스트 실행 전에 최초 한번 실행
        ---
        @BeforeEach : 각 테스트 실행 전에 실행
        @Test
        @AfterEach : 각 테스트 실행 후에 실행
        ---
        @AfterAll : 모든 테스트 실행 후 최초 한번 실행
     */
    // 테스트 메소드 마다 테스트 객체가 만들어지면 BeforeAll 메소드는 static 메소드여야 한다.
        // 그래서 위에서 @TestInstance 설정함. 안했다면 static void initData()여야한다.
    // 한 테스트 객체가 만들어지면 non-static 메소드여야한다.
    @BeforeAll
    void initData(){ //JAVA에서 메소드 호출은 메소드 안에서만 가능.
        existedData.setFeedId(FEED_ID_1);
        existedData.setUserId(USER_ID_2);

        notExistedData.setFeedId(FEED_ID_5);
        notExistedData.setUserId(USER_ID_2);

        /*
        테스트 단계
        given (준비)
        when  (실행)
        then  (체크)

        현재 given 단계
         */
    }


    @Test
    // 중복된 데이터 입력 시 DuplicateKeyException 체크
    void insFeedLikeDuplicatedDataThrowDuplicateKeyException() {
        assertThrows(DuplicateKeyException.class, () -> {
            feedLikeMapper.insFeedLike(existedData);
        }, "데이터 중복 시 에러 발생되지 않음 > Primary key(feed_id, user_id) 확인 바람");
    }

    @Test
    void insFeedLikeNormal(){
        //when
        int actualAffectedRows = feedLikeMapper.insFeedLike(notExistedData);

        //then
        assertEquals(1, actualAffectedRows);
    }

    @Test
    void delFeedLikeNoData(){
        //when
        int actualAffectedRows = feedLikeMapper.delFeedLike(notExistedData);
        //then
        assertEquals(0, actualAffectedRows, "insert 문제 발생");
    }

    @Test
    void delFeedLikeNormal(){
        //when
        int actualAffectedRows = feedLikeMapper.delFeedLike(existedData);
        //then
        assertEquals(1, actualAffectedRows);
    }

    //모든 테스트가 완료된 후에는 commit되지않고 rollback된다.
}