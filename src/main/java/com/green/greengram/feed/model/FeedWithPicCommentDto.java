package com.green.greengram.feed.model;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import lombok.*;

import java.util.List;

//mybatis는 setter를 통해 넣던가 생성자로 넣던가 리플랙션api으로 직접 넣음
// 생성자로 넣는다면 각 컬럼의 순서가 중요해짐, 그래서 setter나 리플랙션 api씀
// 리플랙션api로 넣을거면 NoArgsConstructor 필요.
// 빌더 패턴을 쓸거면 @AllArgsConstructor
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class FeedWithPicCommentDto {
    private long feedId;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private String contents;
    private String location;
    private String createdAt;
    private int isLike;
    private List<String> pics;
    private List<FeedCommentDto> commentList;
}
