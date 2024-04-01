package com.nhnacademy.account.user.repository;

import com.nhnacademy.account.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Boolean existsByEmail(String email);

}
