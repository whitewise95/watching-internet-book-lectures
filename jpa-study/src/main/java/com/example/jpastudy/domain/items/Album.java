package com.example.jpastudy.domain.items;

import com.example.jpastudy.domain.Item;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "A")
public class Album extends Item {

	private String artist;
	private String etc;
}
