package study.together.example.hong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.together.example.hong.entity.Member;
import study.together.example.hong.repository.MemberRepository;
import study.together.example.hong.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void basicCRUD() {
        Member member1 = new Member(1L, "member1", 1);
        Member member2 = new Member(2L, "member2", 2);
        memberService.saveMember(member1);
        memberService.saveMember(member2);

        Member findMember1 = memberService.findMember(member1.getId()).orElse(new Member());
        Member findMember2 = memberService.findMember(member2.getId()).orElse(new Member());

        checkMember(member1, findMember1);
        checkMember(member2, findMember2);

        List<Member> all = memberService.findAll();
        assertThat(all.size()).isEqualTo(2);

        Long count = memberService.count();
        assertThat(count).isEqualTo(2);

        memberService.delete(member1);
        memberService.delete(member2);

        Long deletedCount = memberService.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void save() {
        Member member = new Member(1L, "member1", 1);

        memberService.saveMember(member);
        Member findMember = memberService.findMember(1L).orElse(new Member());

        checkMember(member, findMember);
    }

    @Test
    public void findAllByName() {
        Member member1 = new Member(1L, "member1", 1);
        Member member2 = new Member(2L, "member1", 2);

        memberService.saveMember(member1);
        memberService.saveMember(member2);

        List<Member> findListByName = memberService.findListByName("member1");
        List<Member> findAllByName = memberService.findAllByName("member1");

        assertThat(findListByName.size()).isEqualTo(2);
        assertThat(findAllByName.size()).isEqualTo(2);
    }

    @Test
    public void findByNameAndAge() {
        Member member1 = new Member(1L, "member1", 1);
        Member member2 = new Member(2L, "member2", 2);

        memberService.saveMember(member1);
        memberService.saveMember(member2);

        Member findMember = memberService.findByNameAndAge("member2", 2);
        checkMember(member2, findMember);
    }

    public void checkMember(Member thisMember, Member thatMember) {
        assertThat(thisMember.getId()).isEqualTo(thatMember.getId());
        assertThat(thisMember.getName()).isEqualTo(thatMember.getName());
        assertThat(thisMember.getAge()).isEqualTo(thatMember.getAge());
    }

    @Test
    public void findFirstByOrderByName() {
        Member member1 = new Member(1L, "member1", 1);
        Member member2 = new Member(2L, "member2", 2);

        memberService.saveMember(member1);
        memberService.saveMember(member2);

        Member findMember = memberRepository.findFirstByOrderByNameAsc();
        checkMember(member1, findMember);
    }

    @Test
    public void findTopByOrderByAgeDesc() {
        Member member1 = new Member(1L, "member1", 1);
        Member member2 = new Member(2L, "member2", 2);

        memberService.saveMember(member1);
        memberService.saveMember(member2);

        Member findMember = memberRepository.findTopByOrderByAgeDesc();
        checkMember(member2, findMember);
    }



}
