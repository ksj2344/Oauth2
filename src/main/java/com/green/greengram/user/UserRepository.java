package com.green.greengram.user;

import com.green.greengram.config.security.SignInProviderType;
import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository<T, ID> : <연결할 엔터티, PK타입>
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUidAndProviderType(String uid, SignInProviderType signInProviderType);
    User findByUid(String uid); //findByUid: select * from user where uid= 쿼리를 만들어줌
    //이것을 메소드 쿼리라고 한다.
}
