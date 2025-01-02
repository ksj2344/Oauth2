package com.green.greengram.user.follow;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.user.follow.model.UserFollowReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

//Spring Test Context (컨테이너)를 이용하지 않는다.
@ExtendWith(MockitoExtension.class)
class UserFollowServiceTest {
    @InjectMocks //Mockito가 객체화를 직접 한다.
    UserFollowService userFollowService;

    @Mock //가짜 객체를 만든다. 선언부는 있지만 내용부는 없이 객체화한다.
    UserFollowMapper userFollowMapper;

    @Mock //얘도 userFollowService를 위한 가짜 객체
    AuthenticationFacade authenticationFacade;

    static final long fromUserId1 = 1L;
    static final long toUserId2 = 2L;

    static final long fromUserId3 = 3L;
    static final long toUserId4 = 4L;

    @Test
    @DisplayName("PostUserFollow 테스트")
    void postUserFollow() {
        //given
        final int EXPECTED_RESULT=13;
        final long FROM_USER_ID=fromUserId1;
        final long TO_USER_ID=toUserId2;
            // given import 타입 주의, static import 할 것.
            // 가짜객체 authenticationFacade에게 임시로 할 일을 부여할건데,  given이나 when 씀
            // authenticationFacade Mock객체의 getSignedUserId()메서드를 호출하면 willReturn값을 반환하도록 지시.
        given(authenticationFacade.getSignedUserId()).willReturn(FROM_USER_ID);

        UserFollowReq givenParam=new UserFollowReq(TO_USER_ID);
        givenParam.setFromUserId(FROM_USER_ID);
        given(userFollowMapper.insUserFollow(givenParam)).willReturn(EXPECTED_RESULT);
        //when
        UserFollowReq actualParam =new UserFollowReq(TO_USER_ID);
        int actualResult = userFollowService.postUserFollow(actualParam);

        //then
        assertEquals(EXPECTED_RESULT, actualResult); //mapper로 반환되는 값이 기대한 값과 같은지 확인
    }
    @Test
    @DisplayName("DeleteUserFollow 테스트")
    void deleteUserFollow() {
        //given
        final int EXPECTED_RESULT=14;
        final long FROM_USER_ID=fromUserId3;
        final long TO_USER_ID=toUserId4;
        given(authenticationFacade.getSignedUserId()).willReturn(FROM_USER_ID);

        UserFollowReq givenParam=new UserFollowReq(TO_USER_ID);
        givenParam.setFromUserId(FROM_USER_ID);
        given(userFollowMapper.delUserFollow(givenParam)).willReturn(EXPECTED_RESULT);
        //when
        UserFollowReq actualParam =new UserFollowReq(TO_USER_ID);
        int actualResult = userFollowService.deleteUserFollow(actualParam);

        //then
        assertEquals(EXPECTED_RESULT, actualResult); //mapper로 반환되는 값이 기대한 값과 같은지 확인
    }
}