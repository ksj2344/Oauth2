package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class UpdatedAt extends CreatedAt {
    @LastModifiedDate //update가 됐을 때 현재 일시값을 넣는다. 이 애노테이션이 작동을 하려면 @EntityListeners 세팅이 되어있어야함.
    @Column(nullable = false) //NOT NULL
    private LocalDateTime updatedAt;
}
