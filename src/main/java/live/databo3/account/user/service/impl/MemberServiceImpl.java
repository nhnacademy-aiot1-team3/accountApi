package live.databo3.account.user.service.impl;


import live.databo3.account.user.dto.JoinRequestDto;
import live.databo3.account.user.dto.JoinResponseDto;
import live.databo3.account.user.dto.LoginInfoResponseDto;
import live.databo3.account.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import live.databo3.account.user.repository.MemberRepository;
import live.databo3.account.user.service.MemberService;

import java.util.Objects;

/**
 * MemberService의 구현체
 * @author insub
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     * Repository에 id로 조회 요청 한 뒤, 결과를 반환하는 메서드
     * @param id 조회 하고싶은 Member id
     * @return LoginInfoResponseDto Memeber의 id와 pw로 값이 할당됨
     * @since 1.0.0
     */
    @Override
    public LoginInfoResponseDto getMemberIdAndPassword(String id) {
      
        Member member = memberRepository.findById(id).orElse(null);

        //null 로 변경
        if (Objects.isNull(member)) {
            return null;
        }

        return new LoginInfoResponseDto(member.getId(), member.getPw());
    }

    /**
     * {@inheritDoc}
     * @param email 조회 하고싶은 Member email
     * @return true or false
     * @since 1.0.0
     */
    @Override
    public Boolean isExistByMemberEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     * Member를 create하는 메서드, findById 메서드를 통해 member가 있는지 확인하고
     * 있으면 IllegalStateException을 던짐
     * @param request JoinResquestDto
     * @return member를 save하고 toDto 메서드를 활용해 JoinResponseDto로 변환 후 리턴
     * @throws IllegalStateException 이미 존재하는 회원
     * @since 1.0.0
      */
    @Override
    public JoinResponseDto registerMember(JoinRequestDto request) {
      
        Member member = Member.createMember(request.getId(), passwordEncoder.encode(request.getPassword()), request.getEmail());
      
        if (memberRepository.findById(request.getId()).isPresent()) {
            throw new IllegalStateException("already exist " + member.getId());
        }

        return memberRepository.save(member).toDto();

    }
}
