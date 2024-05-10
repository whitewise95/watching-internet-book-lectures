package com.example.jpastudy.repository;

import com.example.jpastudy.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

	/**
	 * 회원이름
	 * */
	private String memberName;


	/**
	 * 주문상태
	 * 	ORDER, CANCEL
	 */
	private OrderStatus orderStatus;
}
