package study.together.example.hong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.together.example.hong.entity.Member;
import study.together.example.hong.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {


    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public Optional<Member> findMember(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public long count() {
        return memberRepository.count();
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public List<Member> findListByName(String name) {
        return memberRepository.findListByName(name);
    }

    public List<Member> findAllByName(String name) {
        return memberRepository.findAllByName(name);
    }

    public Member findByNameAndAge(String name, Integer age) {
        return memberRepository.findByNameAndAge(name, age);
    }
}
