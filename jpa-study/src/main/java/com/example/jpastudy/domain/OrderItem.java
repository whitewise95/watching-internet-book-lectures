package com.example.jpastudy.domain;

import com.example.jpastudy.jpa.base.BaseDomainWithId;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 상품
 */
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseDomainWithId {

	/**
	 * 구매한 상품 정보
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Item item;

	/**
	 * 주문정보
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private Order order;

	/**
	 * 주문 가격
	 */
	private Integer orderPrice;

	/**
	 * 주문 개수
	 */
	private Integer count;

	// region 생성 메소드
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderPrice(orderPrice);
		orderItem.setItem(item);
		orderItem.setCount(count);

		item.removeStock(count);
		return orderItem;
	}
	// endregion

	// region 비지니스 로직
	public void cancel() {
		getItem().addStock(count);
	}

	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
	// endregion
}
