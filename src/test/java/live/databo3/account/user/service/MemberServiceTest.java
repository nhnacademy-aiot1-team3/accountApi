package live.databo3.account.user.service;

import live.databo3.account.user.dto.*;
import live.databo3.account.user.entity.Member;
import live.databo3.account.user.entity.Roles;
import live.databo3.account.user.entity.States;
import live.databo3.account.user.repository.MemberRepository;
import live.databo3.account.user.repository.RolesRepository;
import live.databo3.account.user.repository.StatesRepository;
import live.databo3.account.user.service.impl.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    RolesRepository rolesRepository;
    @Mock
    StatesRepository statesRepository;
    @Mock
    PasswordEncoder  passwordEncoder;

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

        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_ADMIN);
        States states = new States();
        states.setStateName(States.STATES.WAIT);

        Member member = Member.createMember(memberId,memberPw,memberEmail, roles, states);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        LoginInfoResponseDto loginInfoResponseDto = memberService.getMemberIdAndPassword(memberId);

        Assertions.assertThat(loginInfoResponseDto.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(loginInfoResponseDto.getMemberPassword()).isEqualTo(memberPw);
        Assertions.assertThat(loginInfoResponseDto.getMemberEmail()).isEqualTo(memberEmail);
    }

    @Test
    void isExistByMemberEmail() {

        given(memberRepository.existsByMemberEmail(any())).willReturn(true);

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

        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_ADMIN);
        States states = new States();
        states.setStateName(States.STATES.WAIT);

        Member member = Member.createMember(joinRequestDto.getId(), joinRequestDto.getPassword(), joinRequestDto.getEmail(), roles,states);

        given(rolesRepository.findById(any())).willReturn(Optional.of(roles));
        given(statesRepository.findById(any())).willReturn(Optional.of(states));
        given(memberRepository.save(any())).willReturn(member);

        JoinResponseDto joinResponseDto = memberService.registerMember(joinRequestDto);

        Assertions.assertThat(joinResponseDto.getId()).isEqualTo(member.getMemberId());
        Assertions.assertThat(joinResponseDto.getPassword()).isEqualTo(member.getMemberPassword());
        Assertions.assertThat(joinResponseDto.getEmail()).isEqualTo(member.getMemberEmail());
    }

    @Test
    void modifyMember() throws Exception {

        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_ADMIN);
        States states = new States();
        states.setStateName(States.STATES.WAIT);

        Member member = Member.createMember("memberId", "memberPw", "member@databo3.live", roles, states);

        given(rolesRepository.findById(any())).willReturn(Optional.of(roles));
        given(statesRepository.findById(any())).willReturn(Optional.of(states));
        given(memberRepository.save(any())).willReturn(member);

        Class<UpdateMemberRequestDto> clzz = UpdateMemberRequestDto.class;
        UpdateMemberRequestDto updateMemberRequestDto = clzz.getDeclaredConstructor().newInstance();

        Field memberIdField = clzz.getDeclaredField("id");
        memberIdField.setAccessible(true);
        memberIdField.set(updateMemberRequestDto, "memberId");

        Field memberPwField = clzz.getDeclaredField("password");
        memberPwField.setAccessible(true);
        memberPwField.set(updateMemberRequestDto, "member125135");

        Field memberEmailField = clzz.getDeclaredField("email");
        memberEmailField.setAccessible(true);
        memberEmailField.set(updateMemberRequestDto, "member1344@databo3.live");

        Member modifyMember = Member.createMember(updateMemberRequestDto.getId(), updateMemberRequestDto.getPassword(), updateMemberRequestDto.getEmail(), roles, states);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(memberRepository.save(any())).willReturn(modifyMember);
        given(passwordEncoder.encode(any())).willReturn("member125135");

        UpdateMemberResponseDto updateMemberResponseDto = memberService.modifyMember(updateMemberRequestDto.getId(), updateMemberRequestDto);

        Assertions.assertThat(updateMemberResponseDto.getPassword()).isEqualTo(modifyMember.getMemberPassword());
        Assertions.assertThat(updateMemberResponseDto.getEmail()).isEqualTo(modifyMember.getMemberEmail());

    }
}
