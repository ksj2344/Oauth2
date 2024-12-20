package com.green.greengram.feed.comment;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.feed.comment.model.*;
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
    private final AuthenticationFacade authenticationFacade;

    public long postComment(FeedCommentPostReq p){
        p.setUserId(authenticationFacade.getSignedUserId());
        mapper.insFeedComment(p);
        return p.getFeedCommentId();
    }

    public FeedCommentGetRes getFeedComment(FeedCommentGetReq p){
        FeedCommentGetRes res=new FeedCommentGetRes();
        if(p.getStartIdx()<0){
            res.setCommentList(new ArrayList<>());
            return res;
        }
        List<FeedCommentDto> commentList=mapper.selFeedCommentList(p); //1~21사이의 튜플
        res.setCommentList(commentList);
        res.setMoreComment(commentList.size()==p.getSize());
        if(res.isMoreComment()){
            commentList.remove(commentList.size()-1);
            //commentList가 shallowCopy된거라 이걸로 해도됨
        }
        return res;
    }

    public int delComment(FeedCommentDelReq p){
        p.setUserId(authenticationFacade.getSignedUserId());
        return mapper.delFeedComment(p);
    }
}
