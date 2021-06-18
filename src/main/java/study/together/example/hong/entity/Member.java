package study.together.example.hong.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter @Setter
@Entity
@Table(name = "member")
public class Member {

    @Id
    private Long id;
    private String name;
    private String level;
    private int age;

    public Member() {

    }

    public Member(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Member(Long id, String name, String level, int age) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.age = age;
    }
}
