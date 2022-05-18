package io.timpac.shop.common.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
	private String zipcode;
	private String addr;
	private String addrDetail;
	
	protected Address() {}
	
	public Address(String zipcode, String addr) {
		this.zipcode = zipcode;
		this.addr = addr;
	}
	
	public Address(String zipcode, String addr, String addrDetail) {
		this.zipcode = zipcode;
		this.addr = addr;
		this.addrDetail = addrDetail; 
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getAddr() {
		return addr;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(addr, addrDetail, zipcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(addr, other.addr) && Objects.equals(addrDetail, other.addrDetail)
				&& Objects.equals(zipcode, other.zipcode);
	}
	
}
