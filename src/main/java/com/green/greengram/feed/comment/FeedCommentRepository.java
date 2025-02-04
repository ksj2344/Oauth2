package com.green.greengram.feed.comment;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {


}
