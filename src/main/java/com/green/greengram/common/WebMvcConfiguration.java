package com.green.greengram.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
//@Component도 가능 하다. Component는 빈등록을 하는 모든 에노테이션의 부모 에노테이션
//Configuration: 메소드를 Bean등록하려면 Configuration을 쓰고 메소드 위에 @Bean 써야함
//메소드를 Bean등록하면 메소드가 반환하는 값을 Bean등록하고 반환값을 싱글톤으로 사용한다.
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath;
    //MyFileUtils에서 했던 것 처럼 생성자로 파일 경로 설정
    public WebMvcConfiguration(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //이 메소드는 spring이 호출해줌
        registry.addResourceHandler("/pic/**").addResourceLocations("file:" + uploadPath+"/");

        //새로고침 시 화면이 나타날 수 있도록 세팅
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**")  //classpath가 resources 폴더 말하는거임
                .resourceChain(true)
                .addResolver(new PathResourceResolver(){
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource=location.createRelative(resourcePath);
                        if(resource.exists()&&resource.isReadable()){
                            //리눅스의 경우 파일을 읽기만 가능하거나 쓰기만 가능하거나 권한 지정이 가능함
                        return resource;
                        }
                        return new ClassPathResource("/static/index.html"); //static에 있는 파일이 아닌 경로를 요청하면 index 파일 불러옴
                    }
                });
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //RestController 에노테이션을 붙인 클래스의 모든 URL에 "/api" prefix(경로)를 설정
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
    }
}



//Bean 등록을 하면 리턴하는 주소값을 spring이 갖고있음.
//Spring에서는 활용을 하는것이 목적이라 거의 싱글톤을 사용함. 그러나 저장이 필요할 때는 그때마다 객체를 새로 만들어야함.

