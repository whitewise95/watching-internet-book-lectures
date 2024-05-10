package com.example.jpastudy.domain;

import com.example.jpastudy.exception.NotEnoughStockException;
import com.example.jpastudy.jpa.base.BaseDomainWithId;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품
 */
@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //TODO 공부하기
public abstract class Item extends BaseDomainWithId {

	/**
	 * 상품명
	 */
	private String name;

	/**
	 * 가격
	 */
	private Integer price;

	/**
	 * 재고수량
	 */
	private Integer stockQuantity;

	/**
	 * 카테고리 목록
	 */
	@ManyToMany(mappedBy = "items")
	private List<Category> categoryList = new ArrayList<>();

	//region 비지니스 로직

	/**
	 * stockQuantity 증가
	 * */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	/**
	 * stockQuantity 감소
	 * */
	public void removeStock(int quantity){
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restStock;
	}
	//endregion
}
