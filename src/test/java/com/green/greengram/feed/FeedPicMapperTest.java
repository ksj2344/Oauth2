package com.green.greengram.feed;

import com.green.greengram.config.TestUtils;
import com.green.greengram.feed.like.model.FeedPicVo;
import com.green.greengram.feed.model.FeedPicDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@MybatisTest //모든 mybatis와 연관된 파일을 객체화 해준다
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicMapperTest {
    @Autowired
    FeedPicMapper feedPicMapper;

    @Autowired
    FeedPicTestMapper feedPicTestMapper;

    @Test
    void insFeedPicNoFeedIdThrowForeignKeyException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(10L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("a.jpeg");

        assertThrows(DataIntegrityViolationException.class, () -> {
                feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNullPicsThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);

        assertThrows(MyBatisSystemException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNoPicsThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>());
        assertThrows(BadSqlGrammarException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicPicStringLengthMoreThan50ThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("_123456789_123456789_123456789_123456789_123456789_12");
        assertThrows(BadSqlGrammarException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }


    @Test
    void insFeedPic(){
        String[] pics={"a.jpeg","b.jpeg","c.jpeg"};
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(5L);
        givenParam.setPics(new ArrayList<>(pics.length));
        for(String pic:pics){ givenParam.getPics().add(pic); }
        List<FeedPicVo> feedPicListBefore =feedPicTestMapper.selFeedPicListByFeedId(givenParam.getFeedId());
        int actualAffectedRows = feedPicMapper.insFeedPic(givenParam);
        List<FeedPicVo> feedPicListAfter =feedPicTestMapper.selFeedPicListByFeedId(givenParam.getFeedId());

        // feedPicListAfter에서 pic만 뽑아내서 List<String>으로 변형한 다음 체크한다.
        List<String> feedOnlyPicList=new ArrayList<>(feedPicListAfter.size());
        for(FeedPicVo feedPicVo:feedPicListAfter){ feedOnlyPicList.add(feedPicVo.getPic()); }

        //혹은 스트림을 이용한다.
        List<String> picList=Arrays.asList(pics);
        //Predicate 리턴타입 0 (boolean), 파라미터 0 (FeedPicVo)
        feedPicListAfter.stream().allMatch(feedPicVo -> picList.contains(feedPicVo.getPic()));

        assertAll(
                ()-> feedPicListAfter.forEach(feedPicVo -> TestUtils.assertCurrentTimeStamp(feedPicVo.getCreatedAt())) //반복문돌려 튜플 생성시간 확인
                ,() -> assertEquals(givenParam.getPics().size(), actualAffectedRows)
                ,() -> assertEquals(0, feedPicListBefore.size())

                ,() -> assertTrue(Arrays.asList(pics).containsAll(feedOnlyPicList)) //pics가 feedPicListAfter를 포함하고 있는지
                ,() -> assertTrue(feedOnlyPicList.containsAll(Arrays.asList(pics))) //feedPicListAfter가 pics를 포함하고 있는지
                // Arrays.asList(배열): 배열을 List로 바꿔줌
                // .containsAll(리스트): 파라미터로 받은 list를 포함했느냐?, contains는 단일 객체가 포함되었는지 확인
                ,() -> assertTrue(feedPicListAfter.stream().allMatch(feedPicVo -> picList.contains(feedPicVo.getPic()))) //picList가 더 길어도 포함만 되면 true
                //stream().allMatch() : 하나하나 비교해서 맞는지 확인할 것임.
                ,() -> assertEquals(givenParam.getPics().size(), feedPicListAfter.size()) //그래서 사이즈 파악도 함.

                //이렇게 해도 된다.
                ,() -> assertTrue(feedPicListAfter.stream() //스트림 생성 Stream<FeedPicVo>
                        .map(FeedPicVo::getPic) //똑같은 크기의 새로운 반환 Stream<String> ["a.jpg", "b.jpg", "c.jpg"]
                        .filter(pic -> picList.contains(pic)) //필터는 연산의 결과가 true인 것만 뽑아내서 새로운 스트림 반환 Stream<String> ["a.jpg", "b.jpg", "c.jpg"]
                        .limit(picList.size()) //스트림 크기 제한, 이전 스트림의 크기가 10개인데 limit(2)제한시 크기가 2인 스트림 반환.
                        .count() == picList.size())
                ,() -> assertTrue(feedPicListAfter.stream().map(FeedPicVo::getPic).toList().containsAll(Arrays.asList(pics)))
                // FeedPicVo::getPic가 결국 feedPicVo -> feedPicVo.getPic()와 같음. 위와 아래가 같단소리. 저걸 메서드 참조라고 함. 경우의 수가 하나뿐이면 사용가능.
                // map에 들어가는 파라미터는 람다식으로 만든 Function(함수형 interface) :  return type O (String), parameter O (FeedPicVo)
                ,() -> assertTrue(feedPicListAfter.stream().map(feedPicVo -> feedPicVo.getPic()) // ["a.jpg", "b.jpg", "c.jpg"]
                        .toList() //스트림 > List
                        .containsAll(Arrays.asList(pics)))
        );
    }
}