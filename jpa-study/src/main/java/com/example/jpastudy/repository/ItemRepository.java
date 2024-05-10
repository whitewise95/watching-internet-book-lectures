package com.example.jpastudy.repository;

import com.example.jpastudy.domain.Item;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager em;

	public void save(Item item){
		if (item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item);   //TODO 업데이트비슷한
		}
	}

	public Item findById(Long id){
		return em.find(Item.class, id);
	}

	public List<Item> findAll(){
		return em.createQuery("SELECT I FROM Item I").getResultList();
	}
}
