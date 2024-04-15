package live.databo3.account.user.repository;

import live.databo3.account.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

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
    Boolean existsByMemberEmail(String email);

    /**
     * 데이터베이스에 요청받은 Id 조회하는 메서드
     * @param requestId Request 받은 ID
     * @return Optional<Member>
     */
    Optional<Member> findByMemberId(String requestId);

}
