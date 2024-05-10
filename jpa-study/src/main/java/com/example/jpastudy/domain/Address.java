package com.example.jpastudy.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

/**
 * 주소
 * */
@Embeddable  //TODO 공부하기
@Getter
public class Address {

	/**
	 * 도시
	 * */
	private String city;

	/**
	 * 우편번호
	 * */
	private String zipcode;

	/**
	 * 도로
	 * */
	private String street;

	protected Address() {
	}

	public Address(String city, String zipcode, String street) {
		this.city = city;
		this.zipcode = zipcode;
		this.street = street;
	}
}
