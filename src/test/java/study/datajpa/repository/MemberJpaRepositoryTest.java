package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("기본 테스트")
    public void test() throws Exception {
        // given
        Member member = Member.builder()
                .username("memberA")
                .build();

        // when
        Member savedMember = memberJpaRepository.save(member);

        // then
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("조회 기능")
    public void basicRead() throws Exception {
        // given
        Member member1 = Member.builder()
                .username("memberA")
                .build();
        Member member2 = Member.builder()
                .username("memberB")
                .build();
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        // 리스트 조회
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long countAfterDelete = memberJpaRepository.count();
        assertEquals(countAfterDelete, 0);
    }
    
    @Test
    @Transactional
    @DisplayName("findByUsernameAndAgeGreaterThan")
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        // given
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(25).build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        // when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        // then
        assertEquals(result.get(0).getUsername(), "aaa");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    @Transactional
    @DisplayName("paging test")
    public void paging() throws Exception {
        // given
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(20).build();
        Member m3 = Member.builder().username("ccc").age(20).build();
        Member m4 = Member.builder().username("ddd").age(20).build();
        Member m5 = Member.builder().username("eee").age(20).build();
        Member m6 = Member.builder().username("fff").age(20).build();
        Member m7 = Member.builder().username("ggg").age(20).build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        memberJpaRepository.save(m6);
        memberJpaRepository.save(m7);

        int age = 20;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> result = memberJpaRepository.findByPage(20, 0, 3);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertEquals(result.size(), 3);
        assertEquals(totalCount, 7);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("bulk")
    public void bulkUpdate() throws Exception {
        // given
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(20).build();
        Member m3 = Member.builder().username("ccc").age(20).build();
        Member m4 = Member.builder().username("ddd").age(20).build();
        Member m5 = Member.builder().username("eee").age(20).build();
        Member m6 = Member.builder().username("fff").age(20).build();
        Member m7 = Member.builder().username("ggg").age(20).build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        memberJpaRepository.save(m6);
        memberJpaRepository.save(m7);

        // when
        int updateCount = memberJpaRepository.bulkAgePlus(20);

        // then
        assertEquals(updateCount, 7);
    }

}