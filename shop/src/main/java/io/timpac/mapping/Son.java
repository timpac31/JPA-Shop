package io.timpac.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Son {
	@Id @GeneratedValue
	@Column(name = "SON_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "MOTHER_ID")
	private Mother mother;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Mother getMother() {
		return mother;
	}

	public void setMother(Mother mother) {
		this.mother = mother;
	}
	
}
