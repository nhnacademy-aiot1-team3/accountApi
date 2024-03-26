package user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.dto.LoginRequestDto;
import user.dto.LoginResponseDto;
import user.entity.Member;
import user.repository.MemberRepository;
import user.service.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberIdAndPassword(LoginRequestDto loginRequestDto) {

        return new Member("admin","1234");
    }
}
