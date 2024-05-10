package com.example.jpastudy.service;

import com.example.jpastudy.domain.Address;
import com.example.jpastudy.domain.Member;
import com.example.jpastudy.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	/**
	 * 회원가입
	 */
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	/**
	 * 회원 목록 조회
	 */
	public List<Member> findAll() {
		return memberRepository.findAll();
	}

	/**
	 * 회원 상세 조회
	 */
	public Member findMember(Long id) {
		return memberRepository.findById(id);
	}

	/**
	 * 이름 중복 체크
	 */
	private void validateDuplicateMember(Member member) {
		List<Member> memberList = memberRepository.findByName(member.getName());
		if (!memberList.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원 이름입니다.");
		}
	}
}
