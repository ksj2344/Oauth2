package com.green.greengramver2.feed.comment;

import com.green.greengramver2.feed.FeedMapper;
import com.green.greengramver2.feed.FeedService;
import com.green.greengramver2.feed.comment.model.FeedCommentPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentMapper mapper;

    public long postComment(FeedCommentPostReq p){
        mapper.insFeedComment(p);
        return p.getFeedCommentId();
    }
}
