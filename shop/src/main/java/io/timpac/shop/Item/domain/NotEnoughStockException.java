package io.timpac.shop.Item.domain;

public class NotEnoughStockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotEnoughStockException() {
		super();
	}
	
	public NotEnoughStockException(String msg) {
		super(msg);
	}
}
