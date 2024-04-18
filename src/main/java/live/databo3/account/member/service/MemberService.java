package live.databo3.account.member.service;


import live.databo3.account.member.dto.*;

/**
 * 멤버와 관련된 요청 처리하는 Service interface
 * @author insub
 * @version 1.0.1
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

    /**
     * Member 역할 수정 메서드
     * @param memberId 역할을 수정할 멤버의 아이디
     * @since 1.0.1
     */
    void upgradeRoles(String memberId,Long roleId);

    /**
     * Member 상태 수정 메서드
     * @param memberId 상태를 수정할 멤버의 아이디
     * @since 1.0.1
     */
    void changeState(String memberId);

    /**
     * Member 수정 메서드
     * @param userId Member를 조회하기 위한 param
     * @param updateMemberRequest 수정에 필요한 Dto
     * @return 수정하기 위해 생성한 updateMemberResponse Dto
     * @since 1.0.1
     */
    UpdateMemberResponseDto modifyMember(String userId, UpdateMemberRequestDto updateMemberRequest);

    void deleteMember(String id);
}
