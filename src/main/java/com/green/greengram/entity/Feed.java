package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@Table(name="feed_table") //테이블명이 클래스명이 아닌 다른 이름으로 만들고 싶다면 이렇게
//그런데 클래스명이 카멜케이스라도 테이블은 스네이크 케이스로 만들어짐. 컬럼도 마찬가지. 그래서 그 용도로 쓸 필요는 없음
public class Feed extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;
    @ManyToOne //나To관계  , OneToMany할거면 List로 받아야함.
    @JoinColumn(name="writer_user_id", nullable = false) //참조키 컬럼은 이렇게
    private User writerUser;
    @Column(length = 1_000)
    private String contents;
    @Column(length = 30)
    private String location;

}
