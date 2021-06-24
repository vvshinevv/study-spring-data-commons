package study.together.example.hong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.together.example.hong.entity.Member;
import study.together.example.hong.repository.MemberRepository;
import study.together.example.hong.service.MemberService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    public void findFirst10ByName() {
        Member member1 = new Member(1L, "member1", "C", 1);
        Member member2 = new Member(2L, "member1", "B", 2);
        Member member3 = new Member(3L, "member1", "A", 2);
        Member member4 = new Member(4L, "member1", "C", 1);
        Member member5 = new Member(5L, "member1", "D", 4);
        Member member6 = new Member(6L, "member6", "E", 5);
        Member member7 = new Member(7L, "member7", "F", 6);

        memberService.saveMember(member1);
        memberService.saveMember(member2);
        memberService.saveMember(member3);
        memberService.saveMember(member4);
        memberService.saveMember(member5);
        memberService.saveMember(member6);
        memberService.saveMember(member7);

        List<Member> members = memberRepository.findDistinctTop3ByName("member1");


        List<Member> findMembers = memberRepository.findFirst4ByName("member1", Sort.by(Arrays.asList(new Sort.Order(Sort.Direction.DESC, "level"),
                new Sort.Order(Sort.Direction.ASC, "age"))));

        for (Member member : findMembers) {
            System.out.println("==> " + member.getId() + ", " + member.getLevel() + ", " + member.getAge());
        }
    }

    @Test
    public void findAllByCustomQueryAndStreamTest() {
        Member member1 = new Member(1L, "member1", "C", 1);
        Member member2 = new Member(2L, "member1", "B", 2);
        Member member3 = new Member(3L, "member1", "A", 2);
        Member member4 = new Member(4L, "member1", "C", 1);
        Member member5 = new Member(5L, "member1", "D", 4);
        Member member6 = new Member(6L, "member6", "E", 5);
        Member member7 = new Member(7L, "member7", "F", 6);

        memberService.saveMember(member1);
        memberService.saveMember(member2);
        memberService.saveMember(member3);
        memberService.saveMember(member4);
        memberService.saveMember(member5);
        memberService.saveMember(member6);
        memberService.saveMember(member7);




        try (Stream<Member> streams = memberRepository.findAllByCustomQueryAndStream()) {
            streams.forEach(member -> System.out.println("==> " + member.getId() + ", " + member.getLevel() + ", " + member.getAge()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
