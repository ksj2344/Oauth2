package com.green.greengram.feed.like;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.feed.like.model.FeedLikeReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class FeedLikeServicePostFeedTest {
    @InjectMocks
    private FeedLikeService feedLikeService;
    @Mock
    private FeedLikeMapper feedLikeMapper;
    @Mock
    private AuthenticationFacade authenticationFacade;

    private final long SIGNED_USER_ID_3 = 3L;
    private final long Feed_ID_7=7L;
    private final long Feed_ID_8=8L;

    @BeforeEach
    void setUpAuthenticationFacade() {
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID_3);
    }

    @Test
    @DisplayName("좋아요")
    void feedLikeToggleIns() {
        FeedLikeReq givenParam = new FeedLikeReq();
        givenParam.setUserId(SIGNED_USER_ID_3);
        givenParam.setFeedId(Feed_ID_8);
        given(feedLikeMapper.delFeedLike(givenParam)).willReturn(0);
        given(feedLikeMapper.insFeedLike(givenParam)).willReturn(1);

        FeedLikeReq actualParams = new FeedLikeReq();
        actualParams.setFeedId(Feed_ID_8);
        int actualResult=feedLikeService.feedLikeToggle(actualParams);

        assertEquals(1, actualResult);
    }

    @Test
    @DisplayName("좋아요 취소")
    void feedLikeToggleDel() {
        FeedLikeReq givenParam = new FeedLikeReq();
        givenParam.setUserId(SIGNED_USER_ID_3);
        givenParam.setFeedId(Feed_ID_7);
        given(feedLikeMapper.delFeedLike(givenParam)).willReturn(1);

        FeedLikeReq actualParams = new FeedLikeReq();
        actualParams.setFeedId(Feed_ID_7);
        int actualResult=feedLikeService.feedLikeToggle(actualParams);

        assertEquals(0, actualResult);
    }
}