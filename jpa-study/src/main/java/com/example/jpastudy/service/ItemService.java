package com.example.jpastudy.service;

import com.example.jpastudy.domain.Item;
import com.example.jpastudy.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	/**
	 *	상품 저장
	 */
	@Transactional
	public void saveItem(Item item){
		itemRepository.save(item);
	}

	/**
	 * 상품 목록 조회
	 * */
	public List<Item> findItems(){
		return itemRepository.findAll();
	}

	/**
	 * 상품 상세 조회
	 * */
	public Item findOne(Long id){
		return itemRepository.findById(id);
	}
	
	/**
	 * 상품 수정
	 * */
	@Transactional
	public void updateItem(Long itemId, String name, int price, int stockQuantity) {
		Item item = itemRepository.findById(itemId);
		item.setName(name);
		item.setPrice(price);
		item.setStockQuantity(stockQuantity);
	}
}
