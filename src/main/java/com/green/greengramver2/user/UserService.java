package com.green.greengramver2.user;

import com.green.greengramver2.common.MyFileUtils;
import com.green.greengramver2.user.model.UserSignInReq;
import com.green.greengramver2.user.model.UserSignInRes;
import com.green.greengramver2.user.model.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


}
