package live.databo3.account.user.service.impl;


import live.databo3.account.user.dto.JoinRequestDto;
import live.databo3.account.user.dto.JoinResponseDto;
import live.databo3.account.user.dto.LoginInfoResponseDto;
import live.databo3.account.user.entity.Member;
import live.databo3.account.user.entity.Roles;
import live.databo3.account.user.entity.States;
import live.databo3.account.user.repository.RolesRepository;
import live.databo3.account.user.repository.StatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import live.databo3.account.user.repository.MemberRepository;
import live.databo3.account.user.service.MemberService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * MemberService의 구현체
 * @author insub
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RolesRepository rolesRepository;
    private final StatesRepository statesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     * Repository에 id로 조회 요청 한 뒤, 결과를 반환하는 메서드
     * @param id 조회 하고싶은 Member id
     * @return LoginInfoResponseDto Memeber의 id와 pw로 값이 할당됨
     * @since 1.0.1
     */
    @Override
    public LoginInfoResponseDto getMemberIdAndPassword(String id) {
      
        Member member = memberRepository.findByMemberId(id).orElse(null);

        //null 로 변경
        if (Objects.isNull(member)) {
            return null;
        }

        member.setLastLoginDateTime(LocalDateTime.now());
        memberRepository.save(member);

        return new LoginInfoResponseDto(member.getMemberNumber(),
                member.getMemberId(),
                member.getMemberPassword(),
                member.getMemberEmail(),
                member.getRoles().getRoleName().name(),
                member.getStates().getStateName().name(),
                member.getLastLoginDateTime());
    }

    /**
     * {@inheritDoc}
     * @param email 조회 하고싶은 Member email
     * @return true or false
     * @since 1.0.1
     */
    @Override
    public Boolean isExistByMemberEmail(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    /**
     * {@inheritDoc}
     * Member를 create하는 메서드, findById 메서드를 통해 member가 있는지 확인하고
     * 있으면 IllegalStateException을 던짐
     * @param request JoinResquestDto
     * @return member를 save하고 toDto 메서드를 활용해 JoinResponseDto로 변환 후 리턴
     * @throws IllegalStateException 이미 존재하는 회원
     * @since 1.0.1
      */
    @Override
    public JoinResponseDto registerMember(JoinRequestDto request) {
        Roles roles = rolesRepository.findById(request.getRoles()).orElseThrow(NoSuchElementException::new);
        log.info("회원 가입 권한 : {}", roles);
        States states = statesRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        log.info("회원 가입 상태 : {}", states);

        Member member = Member.createMember(request.getId(), passwordEncoder.encode(request.getPassword()), request.getEmail(), roles, states);
      
        if (memberRepository.findByMemberId(request.getId()).isPresent()) {
            throw new IllegalStateException("already exist " + member.getMemberId());
        }

        return memberRepository.save(member).toDto();

    }
}
