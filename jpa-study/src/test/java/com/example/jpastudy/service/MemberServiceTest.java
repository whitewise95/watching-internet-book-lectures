package com.example.jpastudy.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.example.jpastudy.domain.Member;
import com.example.jpastudy.repository.MemberRepository;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest   //스프핑컨테이너 안에서 돌린다. 없으면 @Autowired가 안됨
@Transactional	  // 테스트이후에 롤백하도록
public class MemberServiceTest {


	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager em;

	@Test
	public void 회원가입() throws Exception {
		//given
		Member member = new Member();
		member.setName("kim");
		member.setUsername("userName");

		//when
		Long savedId = memberService.join(member);

		//then
		em.flush();
		assertEquals(member, memberRepository.findById(savedId));
	}

	@Test(expected = IllegalStateException.class)
	public void 중복_회원_예외() throws Exception {
		//given
		Member member = new Member();
		member.setName("kim");
		member.setUsername("userName");

		Member member2 = new Member();
		member2.setName("kim");
		member2.setUsername("userName");
		//when
		memberService.join(member);
		memberService.join(member2);

		//then
		fail("예외가 발생해야 한다.");


	}
}