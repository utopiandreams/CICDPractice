package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.Inheritance;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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
        Member savedMember = memberRepository.save(member);

        // then
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());
        Member memberget = findMember.get();

        assertEquals(memberget.getId(), member.getId());
        assertEquals(memberget.getUsername(), member.getUsername());
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
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        // 리스트 조회
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

        // 카운트 검증
        long count = memberRepository.count();
        assertEquals(count, 2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long countAfterDelete = memberRepository.count();
        assertEquals(countAfterDelete, 0);
    }

    @Test
    @Transactional
    @DisplayName("findByUsernameAndAgeGreaterThan")
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        // given
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(25).build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        // then
        assertEquals(result.get(0).getUsername(), "aaa");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }
    
    
    @Test
    @DisplayName("findBy")
    public void findBy () throws Exception {
        List<Member> result = memberRepository.findBy();
    }

    @Test
    @DisplayName("findUser")
    public void findUserTest() throws Exception {
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(25).build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("aaa", 20);

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
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertEquals(content.size(), 3);
        assertEquals(page.getNumber(), 0);
        assertEquals(page.getTotalElements(), 7);
        assertEquals(page.getTotalPages(), 3);
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());
    }

    @Test
    @Transactional
    @DisplayName("slice test")
    public void slicing() throws Exception {
        // given
        Member m1 = Member.builder().username("aaa").age(20).build();
        Member m2 = Member.builder().username("bbb").age(20).build();
        Member m3 = Member.builder().username("ccc").age(20).build();
        Member m4 = Member.builder().username("ddd").age(20).build();
        Member m5 = Member.builder().username("eee").age(20).build();
        Member m6 = Member.builder().username("fff").age(20).build();
        Member m7 = Member.builder().username("ggg").age(20).build();
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

        // then
        List<Member> content = slice.getContent();

        assertEquals(content.size(), 3);
        assertEquals(slice.getNumber(), 0);
        assertTrue(slice.isFirst());
        assertTrue(slice.hasNext());
    }

    @Test
    @Transactional
    public void findMemberLazy() throws Exception {
        // given
        Team teamA = Team.builder().name("TeamA").build();
        Team teamB = Team.builder().name("TeamB").build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member m1 = Member.builder().username("aaa").age(20).team(teamA).build();
        Member m2 = Member.builder().username("bbb").age(20).team(teamB).build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        // when
        List<Member> all = memberRepository.findMemberByUsername("aaa");

        for (Member member : all) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
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
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);

        // when
        int updateCount = memberRepository.bulkAgePlus(20);

        // then
        assertEquals(updateCount, 7);
    }

}