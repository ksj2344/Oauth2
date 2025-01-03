package com.green.greengram.feed.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FeedPicSelDto {
    private String pic;
    private long feedId;
}
