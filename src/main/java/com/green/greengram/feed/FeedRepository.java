package com.green.greengram.feed;


import com.green.greengram.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //없어도 자동으로 등록됨
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
