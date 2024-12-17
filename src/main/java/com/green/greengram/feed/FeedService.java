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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //최대한 select를 줄이도록 하는 것이 개발자가 해야할 일. 좋은 방식은 아님. 그래서 아래처럼 처리

    //select 3번으로처리. 피드 5,000개 있음, 페이지당 20개씩 가져온다.
    public List<FeedGetRes> getFeedList2(FeedGetReq p){
        //피드 리스트
        List<FeedGetRes> list=feedMapper.selFeedList(p);

        //feed_id를 골라내야한다.
        List<Long> feedIds=new ArrayList<>(list.size());
        for(FeedGetRes r:list){
            feedIds.add(r.getFeedId());
        }
        /*
         List<Long> feedIds=list.stream().map(FeedGetRes::getFeedId).colloect(Collectors.toList());
    혹은 List<Long> feedIds=list.stream().map(item->((FeedGetRes)item).getFeedId()).toList();
    혹은 List<Long> feedIds=list.stream().map(item->{return ((FeedGetRes)item).getFeedId();}).toList();
        로 쓸 수있음
        stream: 배열이든 list든 hashmap이든 stream으로 변환하면 다루는 방법이 다 같아짐
        stream으로 변환했다 list 변환이 필요해서 for문보다 느림. 근데 여러번 가공할거면 stream쓰는게 낫다.
        .map() : 똑같은 크기의 stream이 나옴.
        (FeedGetRes::getFeedId): 괄호 안을 foreach처럼 복사
        */

        //피드와 관련된 사진 리스트
        List<FeedPicSelDto> feedPicList=feedPicMapper.selFeedPicListByFeedIds(feedIds);
        log.info("feedPicList:{}",feedPicList);

        Map<Long, List<String>> picHashMap=new HashMap<>(); //feedPicMapper로 불러온 값과 feedMapper로 불러온 값을 매칭
        for(FeedPicSelDto item:feedPicList){
            long feedId=item.getFeedId();
            //containsKey(key값) 이 key가 이 Map에 존재하나 확인하는 boolean
            if(!picHashMap.containsKey(feedId)){ //feedId의 key값이 존재하지 않으면~
                picHashMap.put(feedId, new ArrayList<String>(2)); //먼저 feedId key값과 List<String>이 들어갈 공간을 만들어줌
            }
            List<String> pics=picHashMap.get(feedId); //"map주소.get(key값)"은 value의 주소값이 리턴됨
            pics.add(item.getPic());//value(List<String>타입)의 주소에 item의 사진이 추가됨
        }

        //피드와 관련된 댓글 리스트
        List<FeedCommentDto> feedCommentList = feedCommentMapper.selFeedCommentListByFeedIds(feedIds);
        Map<Long, FeedCommentGetRes> commentHashMap=new HashMap<>();
        for(FeedCommentDto item:feedCommentList){
            long feedId=item.getFeedId();
            if(!commentHashMap.containsKey(feedId)){
                FeedCommentGetRes feedCommentGetRes=new FeedCommentGetRes();
                feedCommentGetRes.setCommentList(new ArrayList<>(4));
                commentHashMap.put(feedId, feedCommentGetRes);
            }
            FeedCommentGetRes feedCommentGetRes=commentHashMap.get(feedId); //feedId번째의 hashmap value 주소값 객체
            feedCommentGetRes.getCommentList().add(item); //해당 주소값에 item정보를 담은 리스트 추가
        }

        for(FeedGetRes res : list){
            res.setPics(picHashMap.get(res.getFeedId()));
            FeedCommentGetRes feedCommentGetRes=commentHashMap.get(res.getFeedId());

            if(feedCommentGetRes==null){ //feed에 달린 댓글이 하나도 없다면
                feedCommentGetRes=new FeedCommentGetRes();
                feedCommentGetRes.setCommentList(new ArrayList<>());
            }else if(feedCommentGetRes.getCommentList().size()==4){ //가져온 댓글이 4개라면
                feedCommentGetRes.setMoreComment(true);
                feedCommentGetRes.getCommentList().remove(feedCommentGetRes.getCommentList().size()-1);
            }
            res.setComment(feedCommentGetRes);
        }
        return list;
    }

    //select 2번으로처리
    public List<FeedGetRes> getFeedList3(FeedGetReq p){
        //피드 리스트
        List<FeedGetRes> res=feedMapper.selFeedList(p);

        //피드와 관련된 사진 리스트

        //피드와 관련된 댓글 리스트

        return null;
    }



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
