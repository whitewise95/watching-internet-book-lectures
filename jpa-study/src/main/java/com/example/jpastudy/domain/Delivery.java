package com.example.jpastudy.domain;

import com.example.jpastudy.enums.DeliveryStatus;
import com.example.jpastudy.jpa.base.BaseDomainWithId;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Delivery extends BaseDomainWithId {

	/**
	 * 주문정보
	 */
	@OneToOne(mappedBy = "delivery", orphanRemoval = true, cascade = CascadeType.ALL)
	private Order order;

	/**
	 * 주소
	 */
	@Embedded
	private Address address;

	/**
	 * 배송상태
	 */
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

}
