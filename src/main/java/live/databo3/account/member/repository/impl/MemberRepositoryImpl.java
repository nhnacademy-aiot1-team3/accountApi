package live.databo3.account.member.repository.impl;

import com.querydsl.core.types.Projections;
import live.databo3.account.member.dto.MemberDto;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.QMember;
import live.databo3.account.member.entity.QRoles;
import live.databo3.account.member.entity.QStates;
import live.databo3.account.member.repository.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

/**
 * 멤버테이블 Querydsl
 *
 * @author 양현성
 */
public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    private final QMember member = QMember.member;
    private final QRoles roles = QRoles.roles;
    private final QStates states = QStates.states;

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<MemberDto>> getMemberList(Long roleId, Long stateId) {
        return Optional.ofNullable(from(member)
                .select(Projections.constructor(MemberDto.class,
                        member.memberNumber,
                        member.lastLoginDateTime,
                        member.memberEmail,
                        member.memberId,
                        roles.roleName,
                        states.stateName
                ))
                .leftJoin(roles).on(member.roles.eq(roles))
                .leftJoin(states).on(member.states.eq(states))
                .where(roleId != null ? roles.roleId.eq(roleId) : null)
                .where(stateId != null ? states.stateId.eq(stateId) : null)
                .fetch()
        );
    }
}
