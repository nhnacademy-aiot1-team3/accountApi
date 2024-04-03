package com.nhnacademy.account.user.repository;

import com.nhnacademy.account.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Member JpaRepository interface
 * @author insub
 * @version 1.0.0
 */
public interface MemberRepository extends JpaRepository<Member, String> {

    /**
     * 데이터베이스에 존재하는지 조회하는 메서드
     * @param email Member의 email
     * @return true or false
     */
    Boolean existsByEmail(String email);

}
