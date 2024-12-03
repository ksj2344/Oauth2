package com.green.greengramver2.feed;

import com.green.greengramver2.common.MyFileUtils;
import com.green.greengramver2.feed.comment.FeedCommentMapper;
import com.green.greengramver2.feed.comment.model.FeedCommentDto;
import com.green.greengramver2.feed.comment.model.FeedCommentGetReq;
import com.green.greengramver2.feed.comment.model.FeedCommentGetRes;
import com.green.greengramver2.feed.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper feedMapper;
    private final FeedPicsMapper feedPicsMapper;
    private final FeedCommentMapper feedCommentMapper;
    private final MyFileUtils myFileUtils;


    //트렌젝션=많은 로직을 담고있는 하나의 작업
    //@Transactional: 트렌젝션 실행 중 오류가 날 시 롤백, 무사히 완료하면 커밋하는 에노테이션
    @Transactional
    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){
        int result=feedMapper.insFeed(p);

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
        int resultPics=feedPicsMapper.insPeedPics2(feedPicDto);

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
            res.setPics(feedPicsMapper.selFeedPicList(res.getFeedId()));

            //피드 당 댓굴 4
            //몇번 피드에 어떤 댓글들이 있는지 확인을 위해 feedId설정, //page로 startIdx=0, size=4 설정
            FeedCommentGetReq commentGetReq=new FeedCommentGetReq(res.getFeedId(), 1);
            List<FeedCommentDto> commentList = feedCommentMapper.selFeedCommentList(commentGetReq); //가져올 튜플 최소0, 최대4

            FeedCommentGetRes commentGetRes=new FeedCommentGetRes(); //댓글에 관련한 정보를 담을 객체
            commentGetRes.setCommentList(commentList);
            //댓글 목록이 4개를 넘어가는지 검증. 넘어가면 더보기를 내야하니까.
            commentGetRes.setMoreComment(commentList.size()==commentGetReq.getSize());//맞으면 true, 틀리면 false로 세팅
            //commentGetReq.getSize(): page가 1이면 4, 2이상이면 21로 설정되어있음.
            if(commentGetRes.isMoreComment()){  //isMoreComment(): moreComment의 Getter메소드(JAVA Bean 명명규칙)
                //4개를 넘어도 화면에 뜨는것은 3개이도록 처리
                commentList.remove(commentList.size()-1);
            }
            res.setComment(commentGetRes);
        }
        return list;
    }
    //최대한 select를 줄이도록 하는 것이 개발자가 해야할 일. 좋은 방식은 아님.
}
