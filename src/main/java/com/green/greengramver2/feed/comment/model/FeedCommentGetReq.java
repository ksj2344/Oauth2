package com.green.greengramver2.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.greengramver2.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.BindParam;

/*
    피드 리스트를 가져올 때
    1. 제일 먼저 뜨는 3개의 댓글 : (1) 0,4 <-댓글이 3개를 넘어가는지 확인하기 위해 4개 가져옴(isMore)
    2. 댓글 더보기를 눌렀을 때 가져오는 댓글들 : (2) 3,21 | (3) 23,21 | (4) 43,21
 */
@Getter
@ToString
public class FeedCommentGetReq{
    private final static int DEFAULT_PAGE_SIZE = 20;
    private final static int FIRST_COMMENT_SIZE= 3;
    @Schema(title = "피드PK",name="feed_id",example ="1" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @Schema(title="페이지", description = "2이상의 값만 사용해주세요. 아이템수는 20개입니다.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int page;
    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int size;

    public FeedCommentGetReq(@BindParam("feed_id") long feedId, int page) {
        this.feedId = feedId;
        setPage(page);
    }

    public void setPage(int page){
        this.page = page;
        if(page<1){return;}
        if(page==1){
            startIdx=0;
            size=FIRST_COMMENT_SIZE+1; //+1은 inMore 처리용
            return;
        }
        startIdx = ((page-2)*DEFAULT_PAGE_SIZE)+FIRST_COMMENT_SIZE;
        size=DEFAULT_PAGE_SIZE+1; //+1은 inMore 처리용

        //isMore 처리를 안하면 select 두번해야함
    }
}
