package live.databo3.account.member.repository;

import live.databo3.account.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
//    void testSaveMember() {
//        Member member = new Member();
//        member.setMemberId("member");
//        member.setMemberPassword("1234");
//        member.setMemberEmail("hello@gmail.com");
//
//        memberRepository.save(member);
//
//        Optional<Member> savedMember = memberRepository.findByMemberId("member");
//
//        assertThat(savedMember.get().getMemberId().equals("member"));
//    }
//    @Test
//    @DisplayName("email이 있는지 확인")
//    void testExistsByEmail() {
//        Member member = Member.createMember("hello", "45667", "myname@gmail.com",null,null);
//
//        memberRepository.save(member);
//
//        assertThat(memberRepository.existsByMemberEmail("myname@gmail.com")).isTrue();
//    }
}