package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends UpdatedAt {
    @Id //PK지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment 설정   //GenerationType.AUTO를 사용하면 시퀀스를 부여하게 됨. 이건 Oracle쓸때 씀.
    private Long userId;
    @Column(nullable = false, length = 30)
    private String uid;
    @Column(nullable = false, length = 100)
    private String upw;
    @Column(length = 30)
    private String nickName;
    @Column(length=50)
    private String pic;
}
