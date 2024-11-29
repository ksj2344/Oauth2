package com.green.greengramver2.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

// @Setter가 있으면 spring이 생성자보다 Setter를 우선하여 인식한다.
// 정정: 먼저 생성자에 (0,0)을 보내서 객체 생성 후 Setter를 인식하게 동작한다고 함.
// 그래서 @Setter가 있으면 생성자로 size 20을 넣어줘도 setter를 먼저 인식해서 size를 0으로 인식함
@Getter
@ToString
public class Paging {
    @Schema(example = "1", description = "Selected Page")
    private int page;
    @Schema(example = "30", description = "item count per page")
    private int size;

    @JsonIgnore
    private int startIdx;

    //@JsonIgnore 안써도 final이라서 굳이 프론트에 안뜸
    //사실 이런식으로 final static으로 값을 지정할거면 yaml에 지정하는게 나음.
    private final static int DEFAULT_PAGE_SIZE = 20;

    public Paging(Integer page, Integer size) {
        this.page = (page==null||page<=0)? 1:page;
        this.size = (size==null||size<=0)? DEFAULT_PAGE_SIZE:size;
        this.startIdx = (this.page - 1) * this.size;
    }
}
