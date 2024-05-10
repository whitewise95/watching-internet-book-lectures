package com.example.jpastudy.domain.items;

import com.example.jpastudy.domain.Item;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "B")
public class Book extends Item {

	private String author;
	private String isbn;
}
