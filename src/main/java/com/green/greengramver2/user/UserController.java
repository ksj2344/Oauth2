package com.green.greengramver2.user;

import com.green.greengramver2.common.model.ResultResponse;
import com.green.greengramver2.user.model.UserSignInReq;
import com.green.greengramver2.user.model.UserSignInRes;
import com.green.greengramver2.user.model.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService service;

    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUP(@RequestPart(required = false) MultipartFile pic, @RequestPart UserSignUpReq p) {
        return ResultResponse.<Integer>builder()
                .resultMessage("가입 완료")
                .resultData(service.postSignUp(pic, p))
                .build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq q) {
        UserSignInRes res=service.postSignIn(q);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }
}
