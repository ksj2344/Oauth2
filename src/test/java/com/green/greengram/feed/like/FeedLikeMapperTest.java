package com.green.greengram.feed.like;

import com.green.greengram.config.TestUtils;
import com.green.greengram.feed.like.model.FeedLikeReq;
import com.green.greengram.feed.like.model.FeedLikeVo;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//import static: .Assertions 아래의 static매서드들은 class명 안쓰고 써도 됨.


@ActiveProfiles("test") //yaml 적용되는 파일 선택(application-test.yml)
@MybatisTest //Mybatis Mapper Test이기 때문에 작성 >> Mapper 들이 전부 객체화 >> DI를 할 수 있다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//테스트는 기본적으로 메모리 데이터베이스(H2)를 사용하여 하는데, 메모리 데이터 베이스로 교체하지 않겠다, (AutoConfigureTestDatabase.Replace.NONE)
    // 즉, 우리가 원래 쓰는 데이터베이스로 테스트를 진행하겠다.

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// 이 테스트를 실행할 때마다 클래스(PER_CLASS)단위로 객체화 하겠다.(=테스트 객체를 딱 하나만 만든다.) 기본값음 메서드단위
class FeedLikeMapperTest {
    //테스트는 생성자를 이용한 DI가 불가능
    //DI방법은 필드, setter메소드, 생성자
    //테스트 때는 필드 주입방식을 활용함. (필드 주입 방식일 때는 private 필드에도 주입이 가능)

    @Autowired //스프링 컨테이너가 DI 해주는 에노테이션.
    FeedLikeMapper feedLikeMapper; //필드 주입 방식의 DI

    @Autowired
    FeedLikeTestMapper feedLikeTestMapper;

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
    // 테스트 메소드 마다 테스트 객체가 만들어지면 BeforeAll 메소드는 static 무조건 메소드여야 한다.
        // 위에서 @TestInstance 설정 했다면 static 떼고 void initData()해도 된다.
    // 한 테스트 객체가 만들어지면 non-static 메소드일 수 있다.
    @BeforeAll
    static void initData(){ //JAVA에서 메소드 호출은 메소드 안에서만 가능.
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


    //Test 순서는 중요하지 않다. 병렬로 실행되기 때문. 각각의 테스트는 서로에게 영향을 주지 않는다.

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
        assertEquals(1, actualAffectedRows); //assert : 단원, 체크
    }

    @Test
    void delFeedLikeNoData(){
        //when
        int actualAffectedRows = feedLikeMapper.delFeedLike(notExistedData);
        //then
        assertEquals(0, actualAffectedRows, "insert 문제 발생");
    }

    @Test
    void delFeedLike(){
        //when
        FeedLikeVo actualFeedLikeVoBefore=feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);
        int actualAffectedRows = feedLikeMapper.delFeedLike(existedData);
        FeedLikeVo actualFeedLikeVoAfter=feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);

        //then
        assertAll(
                () -> assertEquals(1, actualAffectedRows)
                ,() -> assertNotNull(actualFeedLikeVoBefore)
                ,() -> assertNull(actualFeedLikeVoAfter)
        );
    }

    @Test
    void insFeedLike() {
        //when
        List<FeedLikeVo> actualFeedLikeListBefore= feedLikeTestMapper.selFeedLikeAll(); //insert 전 기본 튜플 수
        FeedLikeVo actualFeedLikeVoBefore=feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData); //insert 전 PK로 튜플 가져오기
        int actualAffectedRows = feedLikeMapper.insFeedLike(notExistedData);
        FeedLikeVo actualFeedLikeVoAfter=feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData); //insert 전 PK로 튜플 가져오기
        List<FeedLikeVo> actualFeedLikeListAfter= feedLikeTestMapper.selFeedLikeAll(); //insert 후 튜플 수

        //then
        assertAll(
                () -> TestUtils.assertCurrentTimeStamp(actualFeedLikeVoAfter.getCreatedAt()) //튜플 생성시간이 현재시간과 맞는지 검증(오차범위 1초 이하)
                ,() -> assertEquals(actualFeedLikeListBefore.size()+1, actualFeedLikeListAfter.size())
                ,() -> assertNull(actualFeedLikeVoBefore) // 내가 insert하려고 하는 데이터가 없었는지 확인
                ,() -> assertNotNull(actualFeedLikeVoAfter) // 실제 insert가 내가 원하는 데이터로 되었는지 확인
                ,() -> assertEquals(1, actualAffectedRows)
                ,() ->assertEquals(notExistedData.getFeedId(), actualFeedLikeVoAfter.getFeedId()) //원하는 feedId로 insert 되었는지 확인
                ,() ->assertEquals(notExistedData.getUserId(), actualFeedLikeVoAfter.getUserId()) //원하는 userId로 insert 되었는지 확인
        );
    }


    //모든 테스트가 완료된 후에는 commit되지않고 rollback된다.
}