package io.timpac.shop.Item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.timpac.shop.Item.domain.EmptyItem;
import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.repository.ItemRepository;

@Service
@Transactional
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;
	
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	public Item findOne(Long id) {
		return itemRepository.findById(id)
				.orElseGet(EmptyItem::new);
	}
	
	public List<Item> findAll() {
		return itemRepository.findAll();
	}
	
}
