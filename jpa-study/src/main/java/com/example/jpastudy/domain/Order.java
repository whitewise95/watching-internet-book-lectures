package com.example.jpastudy.domain;

import com.example.jpastudy.enums.DeliveryStatus;
import com.example.jpastudy.enums.OrderStatus;
import com.example.jpastudy.exception.NotEnoughStockException;
import com.example.jpastudy.jpa.base.BaseDomainWithId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문
 */
@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseDomainWithId {

	/**
	 * 구매 회원
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Member member;

	/**
	 * 구매 상품 목록
	 */
	@OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	/**
	 * 배송정보
	 */
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Delivery delivery;

	/**
	 * 구매일
	 */
	private LocalDateTime orderDate;

	/**
	 * 상태
	 */
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}


	//region 생성 메소드
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for (OrderItem item : orderItems) {
			order.addOrderItem(item);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}
	//endregion

	// region 비지니스 로직
	/**
	 * 주문 취소
	 * */
	public void cancel(){
		if (this.delivery.getStatus() == DeliveryStatus.COMP){
			throw new NotEnoughStockException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}
		this.setStatus(OrderStatus.CANCEL);
		for (OrderItem orderItem : this.orderItems) {
			orderItem.cancel();
		}
	}
	// endregion

	// region 조회로직
	/**
	 * 전체 주문 가격 조회
	 * */
	public int getTotalPrice(){
		return orderItems.stream()
						 .mapToInt(OrderItem::getTotalPrice)
						 .sum();
	}
	// endregion
}
