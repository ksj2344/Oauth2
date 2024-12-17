package com.green.greengram.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component //빈등록(빈: Spring 컨테이너가 객체관리를 한다.=spring이 대리로 객체화 하고 주소값을 갖고있다가 내가 필요하면 줘라.)
public class MyFileUtils {
    private final String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    /*
            @Value("${file.directory}")은
            yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 값을 넣어준다.
            @Value가 uploadPath에 DI 할 수 있게 해준 것.
         */
    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        log.info("MyFileUtils - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    // path="ddd/aaa"
    // D:/ksj/download/greengram_ver1/ddd/aaa  이렇게 경로가 덧붙여진 파일 만들어줌.
    // 디렉토리 생성
    public void makeFolders(String path){
        File file = new File(uploadPath, path);  //생성자가 두개있어서 uploadPath+"/"+path해도됨
        if(!file.exists()){ //.exists()는 파일이 존재하면 ture를 리턴. 아니면 false를 리턴
            file.mkdirs();
            //mkdir 메소드는 파일안에 파일까지는 감지불가하여 mkdirs를 쓴다.
        }
        //.mkdirs() 는 file 객체로 지정된 파일 경로상 해당 파일이 없다 싶으면 만들어줌.
        // 확장자가 있다면 파일로 인식. 아니라면 디렉토리로 인식한다.

        //getAbsolutePath(): file이 가리키는 경로를 리턴한다.
    }

    //파일명에서 확장자 추출
    public String getExt(String fileName){
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    } //substring 원본에서 잘라내어 새로운 문자열 만들어서 리턴

    /*
        이렇게 하는 사유.
        웹서비스상 영어와 숫자로 조합된 파일명을 써야하므로
        그 규칙에 맞추어 바꿔서 저장해야함.
        단, 확장자는 유지하면서.
    */
    //랜덤파일명 생성
    public String makeRandomFileName(){
        return UUID.randomUUID().toString();
    }

    //랜덤파일명+확장자 생성
    public String makeRandomFileName(String originalFileName){
        return makeRandomFileName()+getExt(originalFileName);
    }

    //MultipartFile 인터페이스의 getOriginalFilename() 구현은 Spring이 알아서 해줌
    //getOriginalFilename() : 내가 올린 원래의 파일 이름
    public String makeRandomFileName(MultipartFile file){
        return makeRandomFileName(file.getOriginalFilename());
    }

    //파일을 원하는 경로(path)에 저장
    public void transferTo(MultipartFile mf, String path) throws IOException {
        if(mf==null){return;}
        File file = new File(uploadPath, path);
        mf.transferTo(file);
        //여기 있는 transferTo는 어딘가에 MultipartFile 인터페이스가 구현화된 클래스의 메소드
    }

    public void deleteFolder(String path, boolean deleteRootFolder){
        File folder = new File(path);
        if(folder.exists()&&folder.isDirectory()){ //폴더가 존재하면서 디렉토리인가?
            // .listFiles() : 객체가 가리키는 디렉토리 안에 있는 모든 파일 및 하위 디렉토리를 배열로 반환
            File[] inculedFiles = folder.listFiles();
            for(File file : inculedFiles){
                if(file.isDirectory()){
                    deleteFolder(file.getAbsolutePath(), true); //재귀호출
                }else {
                    file.delete();
                }
            }
            if(deleteRootFolder){
                folder.delete();
            }
        }

    }
}
