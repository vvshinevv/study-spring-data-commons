package study.together.example.hong.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.together.example.hong.entity.Member;

import java.util.List;
import java.util.stream.Stream;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);
    List<Member> findListByName(String name);
    Member findByNameAndAge(String name, Integer age);

    Member findFirstByOrderByNameAsc();
    Member findTopByOrderByAgeDesc();

    List<Member> findFirst4ByName(String lastname, Sort sort);

    List<Member> findDistinctTop3ByName(String name);

    @Query("select m from Member m")
    Stream<Member> findAllByCustomQueryAndStream();
}
