package com.green.greengram.feed.comment.model;

import com.green.greengram.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

/*
    피드 리스트를 가져올 때
    1. 제일 먼저 뜨는 3개의 댓글 : (1) 0,4 <-댓글이 3개를 넘어가는지 확인하기 위해 4개 가져옴(isMore)
    2. 댓글 더보기를 눌렀을 때 가져오는 댓글들 : (2) 3,21 | (3) 23,21 | (4) 43,21
 */
@Getter
@ToString
@EqualsAndHashCode
public class FeedCommentGetReq{
    @Positive
    @Schema(title = "피드PK",name="feed_id",example ="1" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @PositiveOrZero
    @Schema(title="시작값",description = "댓글 Element갯수",name="start_idx",example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private int startIdx;
    @Min(value = 21, message = "사이즈가 20이상이어야 합니다.")
    @Schema(title="페이지당 아이템 수",description = "default",example = "20")
    private int size;


    //startidx<-프론트엔드에선 현재 댓글 갯수 보내줌. 그게 우리가 시작하는 index가 될거임
    @ConstructorProperties({"feed_id","start_idx","size"})
    public FeedCommentGetReq(long feedId, int startIdx, Integer size) {
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size =(size==null? Constants.getDefaultPageSize() :size)+1;
    }
}
