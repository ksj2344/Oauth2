package com.green.greengram.user;

import com.green.greengram.common.MyFileUtils;
import com.green.greengram.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

    public int postSignUp(MultipartFile pic, UserSignUpReq p) {
        String hashedPassword= BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        p.setUpw(hashedPassword);

        String savedPicName=(pic!=null? myFileUtils.makeRandomFileName(pic) : null);
        p.setPic(savedPicName);

        int result=mapper.insUser(p);

        if(pic==null) { return result; }

        String middlePath=String.format("user/%d",p.getUserId());
        myFileUtils.makeFolders(middlePath);
        String filePath=String.format("%s/%s",middlePath,savedPicName);
        try{
            myFileUtils.transferTo(pic,filePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public UserSignInRes postSignIn(UserSignInReq p) {
        UserSignInRes res= mapper.selUserByUid(p.getUid());
        if(res==null) {
            res =new UserSignInRes();
            res.setMessage("아이디 확인해주세요.");
            return res;
        }else if (!BCrypt.checkpw(p.getUpw(), res.getUpw())) {
            res =new UserSignInRes();
            res.setMessage("비밀번호를 확인해주세요.");
            return res;
        }
        res.setMessage("로그인 성공");
        return res;
    }

    public UserInfoGetRes GetUserInfo(UserInfoGetReq req) {
        return mapper.selUserInfo(req);
    }

    public String patchUserPic (UserPicPatchReq p){
        //1. 저장할 파일명(랜덤한 파일명) 생성한다. 이때, 확장자는 오리지널 파일명과 일치하게 한다.
        String savedPicName=p.getPic() != null? myFileUtils.makeRandomFileName(p.getPic()):null;
        //파일 먼저 만들기(있으면 실행 안하고 없으면 오류남)
        myFileUtils.makeFolders(String.format("user/%d", p.getSignedUserId()));
        //2. 기존 파일 삭제(방법 2가지 [1]: 폴더를 지운다.  [2]:select해서 기존 파일명을 얻어온다. [3]:기존파일명을 FE에서 받는다.)
        String path=String.format("%s/user/%d",myFileUtils.getUploadPath(),p.getSignedUserId());
        myFileUtils.deleteFolder(path, false);
        //3. 원하는 위치에 저장할 파일명으로 파일을 이동(transferTo)한다.
        String filePath=String.format("user/%d/%s",p.getSignedUserId(),savedPicName);
        try {
            myFileUtils.transferTo(p.getPic(),filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4. DB에 잇는 튜플을 수정(Update)한다.
        p.setPicName(savedPicName);
        mapper.updUserPic(p);

        return savedPicName;
    }
}
