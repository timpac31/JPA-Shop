package io.timpac.shop.Item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.timpac.shop.Item.domain.Book;
import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findAll();
		model.addAttribute("items", items);
		
		return "items/itemList";
	}

	@GetMapping("/items/new")
	public String createForm() {
		return "items/itemForm";
	}
	
	@PostMapping("/items/new")
	public String create(Book book) {
		itemService.saveItem(book);
		
		return "redirect:/items";
	}
	
	@GetMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemService.findOne(itemId);
		model.addAttribute("item", item);
		
		return "/items/itemForm";
	}
	
}
