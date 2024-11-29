package com.green.greengramver2.feed;

import com.green.greengramver2.common.MyFileUtils;
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
        int resultPics=feedPicsMapper.insPeedPics(feedPicDto);

        return FeedPostRes.builder()
                .feedId(feedId)
                .pics(picNameList)
                .build();
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p){
        List<FeedGetRes> list=feedMapper.selFeedList(p);

        for(FeedGetRes res : list){
            List<String> picList=feedPicsMapper.selFeedPicList(res.getFeedId());
            res.setPics(picList);
        }
        return list;
    }
}
