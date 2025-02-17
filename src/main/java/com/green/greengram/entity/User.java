package com.green.greengram.entity;

import com.green.greengram.config.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"uid", "provider_type"}
                )
        }
) //복합 unique 설정
public class User extends UpdatedAt {
    @Id //PK지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment 설정   //GenerationType.AUTO를 사용하면 시퀀스를 부여하게 됨. 이건 Oracle쓸때 씀.
    private Long userId; //자동으로 user_id로 명명됨. camelCase로 필드를 작성하더라도 컬럼명이 자동 snake_case로 명명된다

    @Column(nullable = false)
    private SignInProviderType providerType;

    @Column(nullable = false, length = 30) //unique 설정 추가
    private String uid;

    @Column(nullable = false, length = 100)
    private String upw;

    @Column(length = 30) //length는 string에서만 가능
    private String nickName;

    @Column(length= 255)
    private String pic;
}
