package com.green.greengram.feed.like;


import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed/like")
public class FeedLikeController {
    private final FeedLikeService service;

    //원래 뭔가 처리하는건 POST방식이긴한데.. 이경우 GET이 속도가 빨라서 그냥 이거 썼음
    @GetMapping
    public ResultResponse<Integer> feedLikeToggle(@ParameterObject @ModelAttribute FeedLikeReq p){
        log.info("FeedLikeController > feedLikeToggle > p: {}",p);
        int result=service.feedLikeToggle(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result==0? "좋아요 취소":"좋아요 등록")
                .resultData(result)
                .build();
    }
}

// RestController: JSON으로 응답하기위함(Restful의 Rest), CilentSideRendering(CSR)할 때 씀
// Controller쓸 때: SeverSideRendering(SSR)