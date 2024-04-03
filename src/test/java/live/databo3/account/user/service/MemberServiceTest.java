package live.databo3.account.user.service;

import live.databo3.account.user.dto.JoinRequestDto;
import live.databo3.account.user.dto.JoinResponseDto;
import live.databo3.account.user.dto.LoginInfoResponseDto;
import live.databo3.account.user.entity.Member;
import live.databo3.account.user.repository.MemberRepository;
import live.databo3.account.user.service.impl.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMemberIdAndPassword() {

        String memberId = "memberId";
        String memberPw = "memberPw";
        String memberEmail = "member@databo3.live";
        Member member = new Member(memberId, memberPw, memberEmail);

        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        LoginInfoResponseDto loginInfoResponseDto = memberService.getMemberIdAndPassword(memberId);

        Assertions.assertThat(loginInfoResponseDto.getId()).isEqualTo(memberId);
        Assertions.assertThat(loginInfoResponseDto.getPassword()).isEqualTo(memberPw);
    }

    @Test
    void isExistByMemberEmail() {

        given(memberRepository.existsByEmail(any())).willReturn(true);

        String email = "member@databo3.live";
        Boolean result = memberService.isExistByMemberEmail(email);

        Assertions.assertThat(result).isTrue();

    }

    @Test
    void createMember() throws Exception {

        Class<JoinRequestDto> clz = JoinRequestDto.class;
        JoinRequestDto joinRequestDto = clz.getDeclaredConstructor().newInstance();

        Field idField = clz.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(joinRequestDto, "memberId");

        Field passwordField = clz.getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(joinRequestDto, "memberPw");

        Field emailField = clz.getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(joinRequestDto, "member@databo3.live");

        Member member = Member.createMember(joinRequestDto.getId(), joinRequestDto.getPassword(), joinRequestDto.getEmail());

        given(memberRepository.save(any())).willReturn(member);

        JoinResponseDto joinResponseDto = memberService.registerMember(joinRequestDto);

        Assertions.assertThat(joinResponseDto.getId()).isEqualTo(member.getId());
        Assertions.assertThat(joinResponseDto.getPassword()).isEqualTo(member.getPw());
        Assertions.assertThat(joinResponseDto.getEmail()).isEqualTo(member.getEmail());
    }
}
