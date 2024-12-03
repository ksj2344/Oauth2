package com.green.greengramver2.feed.comment;

import com.green.greengramver2.feed.FeedMapper;
import com.green.greengramver2.feed.FeedService;
import com.green.greengramver2.feed.comment.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentMapper mapper;

    public long postComment(FeedCommentPostReq p){
        mapper.insFeedComment(p);
        return p.getFeedCommentId();
    }

    public FeedCommentGetRes getFeedComment(FeedCommentGetReq p){
        FeedCommentGetRes res=new FeedCommentGetRes();
        if(p.getPage()<2){
            res.setCommentList(new ArrayList<>());
            return null;
        }
        List<FeedCommentDto> commentList=mapper.selFeedCommentList(p);
        res.setCommentList(commentList);
        res.setMoreComment(commentList.size()==p.getSize());
        if(res.isMoreComment()){
            commentList.remove(commentList.size()-1);
            //commentList가 shallowCopy된거라 이걸로 해도됨
        }
        return res;
    }

    public int delComment(FeedCommentDelReq p){
        return mapper.delFeedComment(p);
    }
}
