package live.databo3.account.member.service;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.dto.*;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.member.repository.RolesRepository;
import live.databo3.account.member.repository.StatesRepository;
import live.databo3.account.member.service.impl.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    RolesRepository rolesRepository;
    @Mock
    StatesRepository statesRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

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

        assertThat(loginInfoResponseDto.getMemberId()).isEqualTo(memberId);
        assertThat(loginInfoResponseDto.getMemberPassword()).isEqualTo(memberPw);
        assertThat(loginInfoResponseDto.getMemberEmail()).isEqualTo(memberEmail);
    }

    @Test
    void isExistByMemberEmail() {

        given(memberRepository.existsByMemberEmail(any())).willReturn(true);

        String email = "member@databo3.live";
        Boolean result = memberService.isExistByMemberEmail(email);

        assertThat(result).isTrue();

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

        Member member = Member.createMember(joinRequestDto.getId(), joinRequestDto.getPassword(), joinRequestDto.getEmail(),roles,states);

        given(rolesRepository.findById(any())).willReturn(Optional.of(roles));
        given(statesRepository.findById(any())).willReturn(Optional.of(states));
        given(memberRepository.save(any())).willReturn(member);

        JoinResponseDto joinResponseDto = memberService.registerMember(joinRequestDto);

        assertThat(joinResponseDto.getId()).isEqualTo(member.getMemberId());
        assertThat(joinResponseDto.getPassword()).isEqualTo(member.getMemberPassword());
        assertThat(joinResponseDto.getEmail()).isEqualTo(member.getMemberEmail());
    }

    @Test
    @DisplayName("상태 변경 - 멤버 없음")
    void changeStateMemberNotFound() {
        // Given
        String memberId = "invalidMemberId";
        when(memberRepository.findByMemberId(anyString())).thenReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> memberService.changeState(memberId));

        // Then
        assertThat(thrown)
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }


    @Test
    void upgradeMember() throws Exception{

        String memberId = "testId";
        Long roleId = 2L;

        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_VIEWER);
        States states = new States();
        states.setStateName(States.STATES.ACTIVE);

        Member member = Member.createMember("memberId", "memberPw", "member@databo3.live", roles, states);
        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(rolesRepository.findById(any())).willReturn(Optional.of(roles));

        memberService.upgradeRoles(memberId, roleId);

        verify(memberRepository, times(1)).findByMemberId(any());
        verify(rolesRepository, times(1)).findById(any());
        verify(memberRepository, times(1)).save(any());
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

        assertThat(updateMemberResponseDto.getPassword()).isEqualTo(modifyMember.getMemberPassword());
        assertThat(updateMemberResponseDto.getEmail()).isEqualTo(modifyMember.getMemberEmail());

    }

    @Test
    void deleteMember() throws Exception {

        String id = "testId";

        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_VIEWER);
        States states = new States();
        states.setStateName(States.STATES.ACTIVE);

        States quit = new States();
        quit.setStateId(4L);
        quit.setStateName(States.STATES.QUIT);

        Member member = Member.createMember(id, "123", "test@test.live", roles, states);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(statesRepository.findById(any())).willReturn(Optional.of(quit));

        memberService.deleteMember(id);
        verify(memberRepository, times(1)).save(member);
    }
}
