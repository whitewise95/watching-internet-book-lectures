package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    //SpringDataJpa에서 제공하지 않는 메소드는 이 처럼 @Query 를 사용합니다.
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

}
