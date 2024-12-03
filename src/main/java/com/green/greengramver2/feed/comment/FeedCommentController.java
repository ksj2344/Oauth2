package com.green.greengramver2.feed.comment;

import com.green.greengramver2.common.model.ResultResponse;
import com.green.greengramver2.feed.comment.model.FeedCommentGetReq;
import com.green.greengramver2.feed.comment.model.FeedCommentGetRes;
import com.green.greengramver2.feed.comment.model.FeedCommentPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed/comment")
public class FeedCommentController {
    private final FeedCommentService service;

    @PostMapping
    public ResultResponse<Long> postComment(@RequestBody FeedCommentPostReq p){
        log.info("FeedCommentController>postComment>p {}", p);
        return ResultResponse.<Long>builder()
                .resultMessage("댓글 등록 완료")
                .resultData(service.postComment(p))
                .build();
    }

    @GetMapping
    public ResultResponse<FeedCommentGetRes> getFeedCommentList(@ParameterObject FeedCommentGetReq p){
        log.info("FeedCommentController>getFeedCommentList>p {}", p);
        FeedCommentGetRes res = service.getFeedComment(p);
        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d개의 댓글",res.getCommentList().size()))
                .resultData(res)
                .build();
    }
}
