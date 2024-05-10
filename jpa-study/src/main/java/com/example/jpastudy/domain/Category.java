package com.example.jpastudy.domain;

import com.example.jpastudy.jpa.base.BaseDomainWithId;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리
 */
@Getter
@Setter
@Entity
public class Category extends BaseDomainWithId {

	/**
	 * 카테고리 명
	 */
	private String name;

	/**
	 * 상품목록
	 */
	@ManyToMany
	@JoinTable(name = "category_item",
		joinColumns = @JoinColumn(name = "category_id"),
		inverseJoinColumns = @JoinColumn(name = "item_id"))
	private List<Item> items = new ArrayList<>();

	/**
	 * 상위 카테고리
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;

	/**
	 * 하위 카테고리 목록
	 */
	@OneToMany
	private List<Category> child = new ArrayList<>();
}
