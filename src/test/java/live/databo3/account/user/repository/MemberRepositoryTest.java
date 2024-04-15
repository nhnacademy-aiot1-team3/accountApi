package live.databo3.account.user.repository;

import live.databo3.account.user.entity.Member;
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

    @Test
    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
    void testSaveMember() {
        Member member = new Member();
        member.setId("member");
        member.setPw("1234");
        member.setEmail("hello@gmail.com");

        memberRepository.save(member);

        Optional<Member> savedMember = memberRepository.findById("member");

        assertThat(savedMember.get().getId().equals("member"));
    }
    @Test
    @DisplayName("email이 있는지 확인")
    void testExistsByEmail() {
        Member member = new Member("hello", "45667", "myname@gmail.com");

        memberRepository.save(member);

        assertThat(memberRepository.existsByEmail("myname@gmail.com")).isTrue();
    }
}