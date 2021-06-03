package study.together.example.hong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.together.example.hong.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);
    List<Member> findListByName(String name);
    Member findByNameAndAge(String name, Integer age);

    Member findFirstByOrderByNameAsc();
    Member findTopByOrderByAgeDesc();
}
