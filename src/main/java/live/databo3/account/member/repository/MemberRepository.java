package live.databo3.account.member.repository;

import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Member JpaRepository interface
 * @author insub
 * @version 1.0.0
 */
public interface MemberRepository extends JpaRepository<Member, Long>,MemberRepositoryCustom {

    /**
     * 데이터베이스에 존재하는지 조회하는 메서드
     * @param email Member의 email
     * @return true or false
     */
    Boolean existsByMemberEmail(String email);

    /**
     * 데이터베이스에 요청받은 Id 조회하는 메서드
     * @param requestId Request 받은 ID
     * @return Optional<Member>
     */
    Optional<Member> findByMemberId(String requestId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.states = :pause WHERE m.lastLoginDateTime <= :cutoffDateTime AND m.states != :pause")
    int updateStatesByLastLoginTime(LocalDateTime cutoffDateTime, States pause);

}
