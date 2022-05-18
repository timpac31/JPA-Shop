package io.timpac.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Mother {
	@Id @GeneratedValue
	@Column(name = "MOTHER_ID")
	private Long id;
	
	@OneToMany(mappedBy = "mother", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Son> son = new ArrayList<>();

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public List<Son> getSon() {
		return son;
	}
	
	public void setSon(List<Son> son) {
		this.son = son;
	}

}
