package com.example.jpastudy.service;

import com.example.jpastudy.domain.Address;
import com.example.jpastudy.domain.Item;
import com.example.jpastudy.domain.Member;
import com.example.jpastudy.domain.Order;
import com.example.jpastudy.domain.items.Book;
import com.example.jpastudy.enums.OrderStatus;
import com.example.jpastudy.exception.NotEnoughStockException;
import com.example.jpastudy.repository.OrderRepository;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

	@Autowired
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		//given
		Member member = createMember();
		Book book = createBook("시골 JPA", 10000, 10);
		int orderCount = 2;
		//when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		//then
		Order getOrder = orderRepository.findById(orderId);
		Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		Assert.assertEquals("주문한 상품 종류 수가 정확해야한다.", 1, getOrder.getOrderItems().size());
		Assert.assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
		Assert.assertEquals("주문 수량만큼 재고가 줄어야한다.", (Integer) 8, book.getStockQuantity());

	}

	private Book createBook(String bookName, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(bookName);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "123-123", "강가"));
		em.persist(member);
		return member;
	}

	@Test(expected = NotEnoughStockException.class) //TODO 글쓰기
	public void 상품주문_재고수량초과() throws Exception {
		//given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		int orderCount = 11;

		//when
		orderService.order(member.getId(), item.getId(), orderCount);

		//then
		Assert.fail("재고 수량 부족 예외가 발생해야 한다.");
	}

	@Test
	public void 주문취소() throws Exception {
		//given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		//when
		orderService.cancelOrder(orderId);

		//then
		Order getOrder = orderRepository.findById(orderId);
		Assert.assertEquals("상품 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
		Assert.assertEquals("주문이 취소된 상품은 재고가 돌아와야한다.", (Integer) 10, item.getStockQuantity());
	}
}