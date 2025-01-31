package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass //Entity 부모역할
@EntityListeners(AuditingEntityListener.class) //Listener는 이벤트와 연결할 때 붙음(event binding)
public class CreatedAt {
    //컬럼 에노테이션은 엔터티 멤버필드에 자동으로 붙으나 설정을 좀 더 하고 싶다면 붙여야한다.
    @CreatedDate //insert가 됐을 때 현재 일시값을 넣는다. 이 애노테이션이 작동을 하려면 @EntityListeners 세팅이 되어있어야함.
    @Column(nullable = false) //NOT NULL
    private LocalDateTime createdAt; //DATETIME용. 실제로 테이블을 만들것이라
}

//insert와 update시 값을 넣어주는 이벤트를 넣으려면 Application에 @EnableJpaAuditing를 설정해주어야한다.

//createdAt과 updatedAt 자주 쓰이는 칼럼이므로 상속으로 처리하기로 함.