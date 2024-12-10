package com.green.greengram.feed;

import com.green.greengram.common.MyFileUtils;
import com.green.greengram.feed.comment.FeedCommentMapper;
import com.green.greengram.feed.comment.model.FeedCommentDto;
import com.green.greengram.feed.comment.model.FeedCommentGetReq;
import com.green.greengram.feed.comment.model.FeedCommentGetRes;
import com.green.greengram.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper feedMapper;
    private final FeedPicMapper feedPicMapper;
    private final FeedCommentMapper feedCommentMapper;
    private final MyFileUtils myFileUtils;

    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){
        feedMapper.insFeed(p);

        // 파일 등록
        // D:/ksj/download/greengram_ver2/feed/${feedId}/파일명
        long feedId=p.getFeedId();
        String middlePath=String.format("feed/%d",feedId);
        myFileUtils.makeFolders(middlePath);

        //랜덤파일명 저장용 >> feed_pics 테이블에 저장할 때 사용
        List<String> picNameList=new ArrayList<>(pics.size());
        for(MultipartFile pic : pics){
            String savePicName=myFileUtils.makeRandomFileName(pic);
            picNameList.add(savePicName);
            String filePath=String.format("%s/%s",middlePath,savePicName);
            try{
                myFileUtils.transferTo(pic, filePath);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        FeedPicDto feedPicDto=new FeedPicDto();
        feedPicDto.setFeedId(feedId);
        feedPicDto.setPics(picNameList);
        feedPicMapper.insFeedPic(feedPicDto);

        return FeedPostRes.builder()
                .feedId(feedId)
                .pics(picNameList)
                .build();
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p){
        // N+1 이슈 발생
        List<FeedGetRes> list=feedMapper.selFeedList(p);
        //피드 가져오기
        for(FeedGetRes res : list){
            //피드 당 사진 리스트
            res.setPics(feedPicMapper.selFeedPicList(res.getFeedId()));

            //피드 당 댓글 4
            //몇번 피드에 어떤 댓글들이 있는지 확인을 위해 생성자로 feedId설정
            FeedCommentGetReq commentGetReq=new FeedCommentGetReq(res.getFeedId(), 0,3);
            List<FeedCommentDto> commentList = feedCommentMapper.selFeedCommentList(commentGetReq); //가져올 튜플 최소0, 최대4

            FeedCommentGetRes commentGetRes=new FeedCommentGetRes();
            commentGetRes.setCommentList(commentList);
            //댓글 목록이 4개를 넘어가는지 검증. 넘어가면 더보기를 내야하니까.
            commentGetRes.setMoreComment(commentList.size()==commentGetReq.getSize());//맞으면 true, 틀리면 false로 세팅
            //commentGetReq.getSize(): startIdx가 0이면 4, 4이상이면 21로 설정되어있음.
            if(commentGetRes.isMoreComment()){  //isMoreComment(): moreComment의 Getter메소드(JAVA Bean 명명규칙)
                //4개를 넘어도 화면에 뜨는것은 3개이도록 처리
                commentList.remove(commentList.size()-1);
            }
            res.setComment(commentGetRes);
        }
        return list;
    }
    //최대한 select를 줄이도록 하는 것이 개발자가 해야할 일. 좋은 방식은 아님.


    @Transactional
    public int deleteFeed(FeedDeleteReq p) {
        //피드 댓글, 좋아요, 사진삭제
        int affectedRows = feedMapper.delFeedLikeAndFeedCommentAndFeedPic(p);
        log.info("deleteFeed > affectedRows: {}", affectedRows); //사진때문에 0이 될 수 없음

        //피드 삭제
        int affectedRowsFeed =feedMapper.delFeed(p);
        log.info("deleteFeed > affectedRowsFeed: {}", affectedRowsFeed); //사진때문에 0이 될 수 없음

        //피드 사진 삭제
        String deletePath = String.format("%s/feed/%d", myFileUtils.getUploadPath(), p.getFeedId());
        myFileUtils.deleteFolder(deletePath, true);

        return affectedRowsFeed;
    }
}
