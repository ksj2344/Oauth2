package com.green.greengram.feed.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.feed.comment.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;


import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = FeedCommentController.class
        , excludeAutoConfiguration = SecurityAutoConfiguration.class
) //controller만 객체화 한다는 뜻.
// @Import({FeedLikeService.class})  //진짜 객체를 넣으려면 이렇게.
class FeedCommentControllerTest {
    @Autowired
    ObjectMapper objectMapper; //JSON사용
    @Autowired
    MockMvc mockMvc; //요청(보내고)-응답(받기) 처리
    @MockBean //가짜 객체를 만들고 빈등록한다.
    FeedCommentService feedCommentService;  //commentConroller 객체화 하려고 씀

    final long feedId_2 = 2L;
    final long feedCommentId_3 = 3L;
    final long writerUserId_4 = 4L;
    final long SIZE = 20;
    final String BASE_URL ="/api/feed/comment";

    @Test
    @DisplayName("피드 댓글 등록 테스트")
    void postFeedComment() throws Exception {
        FeedCommentPostReq givenParam = new FeedCommentPostReq();
        givenParam.setFeedId(feedId_2);
        givenParam.setComment("코멘트");

        given(feedCommentService.postComment(givenParam)).willReturn(feedCommentId_3);

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions=mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                                                       .content(paramJson));

        ResultResponse res = ResultResponse.<Long>builder()
                .resultData(feedCommentId_3)
                .resultMessage("댓글 등록 완료")
                .build();
        String expectedResJson= objectMapper.writeValueAsString(res);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(feedCommentService).postComment(givenParam);
    }

    @Test
    @DisplayName("피드 보기 테스트")
    void getFeedCommentList() throws Exception {

        FeedCommentGetReq givenParam=new FeedCommentGetReq(feedId_2,1,20);

        FeedCommentDto feedCommentDto=new FeedCommentDto();
        feedCommentDto.setFeedCommentId(feedCommentId_3);
        feedCommentDto.setFeedId(feedId_2);
        feedCommentDto.setWriterUserId(writerUserId_4);
        feedCommentDto.setComment("고멘도");
        feedCommentDto.setWriterNm("작성자");
        feedCommentDto.setWriterPic("profile.pic");

        FeedCommentGetRes expectedResult=new FeedCommentGetRes();
        expectedResult.setMoreComment(false);
        expectedResult.setCommentList(List.of(feedCommentDto));


        //service.getFeedComment에 임무부여
        given(feedCommentService.getFeedComment(givenParam)).willReturn(expectedResult);


        ResultActions resultActions = mockMvc.perform(get(BASE_URL).queryParams(getParameter(givenParam)));

        String expectedResJson = getExpectedResJson(expectedResult);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(feedCommentService).getFeedComment(givenParam);
    }
    private MultiValueMap<String, String> getParameter(FeedCommentGetReq givenParam) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("feed_id", String.valueOf(givenParam.getFeedId()));
        queryParams.add("start_idx", String.valueOf(givenParam.getStartIdx()));
        queryParams.add("size", String.valueOf(SIZE));
        return queryParams;

        //?feedId=2&key=value&name=hong
    }

    private String getExpectedResJson(FeedCommentGetRes res) throws Exception {
        ResultResponse expectedRes = ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d개의 댓글",res.getCommentList().size()))
                .resultData(res)
                .build();
        return objectMapper.writeValueAsString(expectedRes); //JSON형태의 문자열로 변환(직렬화)
    }


    @Test
    @DisplayName("피드 댓글 삭제")
    void delFeedComment() throws Exception {
        final int RESULT=3;
        FeedCommentDelReq givenParam=new FeedCommentDelReq(feedCommentId_3);

        given(feedCommentService.delComment(givenParam)).willReturn(RESULT);

        ResultActions resultActions=mockMvc.perform(delete(BASE_URL)
                .queryParam("feed_comment_id", String.valueOf(givenParam.getFeedCommentId())));
        //queryParams는 MultiValueMap타입을 받고 queryParam은 직접받음. 이어서 써도 됨.

        String expectedResJson=objectMapper.writeValueAsString(ResultResponse.<Integer>builder()
                                                                                .resultData(RESULT)
                                                                                .resultMessage("댓글 삭제")
                                                                                .build()); //메세지도 같아야함..
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(feedCommentService).delComment(givenParam);

    }
}