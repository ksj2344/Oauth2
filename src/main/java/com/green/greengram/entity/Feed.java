package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feed extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;
    @ManyToOne //나To관계  , OneToMany할거면 List로 받아야함.
    @JoinColumn(name="writer_user_id", nullable = false) //참조키 컬럼은 이렇게
    private User user;
    @Column(length = 1_000)
    private String contents;
    @Column(length = 30)
    private String location;

}
