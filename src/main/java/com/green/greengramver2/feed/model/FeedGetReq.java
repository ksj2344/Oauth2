package com.green.greengramver2.feed.model;

import com.green.greengramver2.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.BindParam;

import java.beans.ConstructorProperties;

@Slf4j
@Getter
@ToString(callSuper = true)
public class FeedGetReq extends Paging {
    //name="signed_user_id" swagger에서 이렇게 날림
    @Schema(title="로그인 유저PK", name="signed_user_id",example = "1", requiredMode=Schema.RequiredMode.REQUIRED)
    private long signedUserId;

    //@BindParam : "signed_user_id" 이렇게 요청 들어오면 애노테이션 붙은 파라미터랑 연결함
    //@ConstructorProperties({"page","size","signed_user_id"}) <-@BindParam식으로 여러개 하려면 일케
    public FeedGetReq(Integer page, Integer size, @BindParam("signed_user_id") long userId) {
        super(page, size);
        signedUserId = userId;
        log.info("page: {}, size: {}, userId: {}", page, size, userId);
    }
}
