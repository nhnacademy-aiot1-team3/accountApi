package live.databo3.account.member.repository;

import live.databo3.account.member.dto.MemberDto;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Querydsl 사용을 위한 커스텀 repository
 * @author 양현성
 */

@NoRepositoryBean
public interface MemberRepositoryCustom {

    /**
     * roleId 와 stateId 를 입력받아 해당하는 멤버 전체 조회하는 메서드
     * @param roleId
     * @param stateId
     * @return 해당하는 멤버 전체
     */
    Optional<List<MemberDto>> getMemberList(Long roleId, Long stateId);
}
