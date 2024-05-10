package com.example.jpastudy.domain;

import com.example.jpastudy.jpa.base.BaseDomainWithId;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Entity
public class Member extends BaseDomainWithId {

	/**
	 * 아이디
	 */
	@NonNull
	private String username;

	/**
	 * 이름
	 * */
	@Column(unique = true)
	@NonNull
	private String name;

	/**
	 * 주소
	 */
	@Embedded
	private Address address;

	/**
	 * 구매목록
	 */
	@OneToMany(mappedBy = "member", orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();
}
