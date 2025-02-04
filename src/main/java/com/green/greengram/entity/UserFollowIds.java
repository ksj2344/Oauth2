package com.green.greengram.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class UserFollowIds implements Serializable {
    private Long fromUserId;
    private Long toUserId;
}

/*
    복합키 객체를 만들때 세가지
    1. @Embeddable
    2. @EqualsAndHashCode
    3. implements Serializable
 */
