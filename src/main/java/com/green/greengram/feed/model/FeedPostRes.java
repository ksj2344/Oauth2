package com.green.greengram.feed.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(title="피드 등록 응답")
@EqualsAndHashCode
public class FeedPostRes {
    @Schema(title="피드 PK")
    private long feedId;
    @Schema(title="피드 사진 리스트")
    private List<String> pics; //썸네일 처리를 했을시 줘야함
}
