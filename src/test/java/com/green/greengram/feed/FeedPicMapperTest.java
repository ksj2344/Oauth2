package com.green.greengram.feed;

import com.green.greengram.feed.like.FeedLikeMapper;
import com.green.greengram.feed.model.FeedPicDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicMapperTest {
    @Autowired
    private FeedPicMapper feedpicmapper;

    @Test
    void insFeedPicNoFeedIdThrowForeignKeyException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(10L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("a.jpeg");

        assertThrows(DataIntegrityViolationException.class, () -> {
                feedpicmapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNullPicsThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);

        assertThrows(MyBatisSystemException.class, () -> {
            feedpicmapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNoPicsThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>());
        assertThrows(BadSqlGrammarException.class, () -> {
            feedpicmapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicPicStringLengthMoreThan50ThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("_123456789_123456789_123456789_123456789_123456789_12");
        assertThrows(BadSqlGrammarException.class, () -> {
            feedpicmapper.insFeedPic(givenParam);
        });
    }


    @Test
    void insFeedPic(){
        String[] pics={"a.jpeg","b.jpeg","c.jpeg"};
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>(pics.length));
        for(String pic:pics){ givenParam.getPics().add(pic); }

        int actualAffectedRows = feedpicmapper.insFeedPic(givenParam);
        assertEquals(givenParam.getPics().size(), actualAffectedRows);
    }
}