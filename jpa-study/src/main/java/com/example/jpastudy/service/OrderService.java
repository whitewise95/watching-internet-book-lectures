package com.example.jpastudy.service;

import com.example.jpastudy.domain.Delivery;
import com.example.jpastudy.domain.Item;
import com.example.jpastudy.domain.Member;
import com.example.jpastudy.domain.Order;
import com.example.jpastudy.domain.OrderItem;
import com.example.jpastudy.enums.DeliveryStatus;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.repository.OrderRepository;
import com.example.jpastudy.repository.OrderSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {


	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;


	/**
	 * 주문
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		// region 엔티티 조회 
		Member member = memberRepository.findById(memberId);
		Item item = itemRepository.findById(itemId);
		// endregion

		// region 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		// endregion

		// region 주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		// endregion

		// region 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		orderRepository.save(order);
		return order.getId();
		// endregion
	}

	/**
	 * 주문 취소
	 * */
	@Transactional
	public void cancelOrder(Long orderId){
		Order order = orderRepository.findById(orderId);
		order.cancel();
	}

	/**
	 *	검색
	 * */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);
	}
}
