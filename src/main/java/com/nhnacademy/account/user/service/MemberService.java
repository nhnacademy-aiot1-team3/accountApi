package com.nhnacademy.account.user.service;


import com.nhnacademy.account.user.dto.JoinRequestDto;
import com.nhnacademy.account.user.dto.JoinResponseDto;
import com.nhnacademy.account.user.dto.LoginInfoResponseDto;

/**
 * 멤버와 관련된 요청 처리하는 Service interface
 * @author insub
 * @version 1.0.0
 */
public interface MemberService {

    /**
     * 멤버를 조회 메서드
     * @param id 조회 하고싶은 Member id
     * @return 데이터베이스에 id로 검색한 결과의 Member Dto
     * @since 1.0.0
     */
    LoginInfoResponseDto getMemberIdAndPassword(String id);

    /**
     * 존재하는 member인지 조회 메서드
     * @param email 조회 하고싶은 Member email
     * @return 데이터베이스에 email로 검색한 결과의 Boolean
     * @since 1.0.0
     */
    Boolean isExistByMemberEmail(String email);

    /**
     * Member 생성 메서드
     * @param request 생성 하고싶은 Member request Dto
     * @return 데이터베이스에 생성된 Member Response Dto
     * @since 1.0.0
     */
    JoinResponseDto registerMember(JoinRequestDto request);
}
