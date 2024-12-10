package com.green.greengram.feed.comment;

import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.feed.comment.model.FeedCommentDelReq;
import com.green.greengram.feed.comment.model.FeedCommentGetReq;
import com.green.greengram.feed.comment.model.FeedCommentGetRes;
import com.green.greengram.feed.comment.model.FeedCommentPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "피드 댓글 리스트", description = "댓글 더보기 처리")
    public ResultResponse<FeedCommentGetRes> getFeedCommentList(@ParameterObject FeedCommentGetReq p){
        log.info("FeedCommentController>getFeedCommentList>p {}", p);
        FeedCommentGetRes res = service.getFeedComment(p);
        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d개의 댓글",res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    @GetMapping("/ver2")
    @Operation(summary = "피드 댓글 리스트", description = "댓글 더보기 처리")
    public ResultResponse<FeedCommentGetRes> getFeedComment2(@Parameter(description = "피드 PK", example = "12") @RequestParam("feed_id") long feedId
            , @Parameter(description = "페이지", example = "2") @RequestParam int startIdx, @RequestParam Integer size
    ) {
        FeedCommentGetReq p = new FeedCommentGetReq(feedId,startIdx,size);
        log.info("FeedCommentController > getFeedComment > p: {}", p);
        FeedCommentGetRes res = service.getFeedComment(p);
        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    //삭제시 받아야 할 데이터: feedCommentId + 로그인한 사용자의 PK  (feed_comment_id, signed_user_id)
    @DeleteMapping
    public ResultResponse<Integer> delFeedComment(@ParameterObject FeedCommentDelReq p){
        log.info("FeedCommentController > delFeedComment > p: {}", p);
        return ResultResponse.<Integer>builder()
                .resultMessage("댓글 삭제")
                .resultData(service.delComment(p))
                .build();
    }

}
