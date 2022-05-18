package io.timpac.mapping;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParentId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "PARENT_ID1")
	private String id1;
	
	@Column(name = "PARENT_ID2")
	private String id2;

	public ParentId() {
	}

	public ParentId(String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id1, id2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParentId other = (ParentId) obj;
		return Objects.equals(id1, other.id1) && Objects.equals(id2, other.id2);
	}
}
