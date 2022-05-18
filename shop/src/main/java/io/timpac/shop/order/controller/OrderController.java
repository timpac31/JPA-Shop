package io.timpac.shop.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.timpac.shop.Item.domain.Item;
import io.timpac.shop.Item.service.ItemService;
import io.timpac.shop.order.domain.Customer;
import io.timpac.shop.order.domain.Order;
import io.timpac.shop.order.service.CustomerService;
import io.timpac.shop.order.service.OrderService;

@Controller
public class OrderController {
	@Autowired private OrderService orderService;	
	@Autowired private CustomerService customerService;
	@Autowired private ItemService itemService;
	
	@GetMapping("/order")
	public String orderForm(Model model) {
		List<Customer> customers = customerService.findCustomers();
		List<Item> items = itemService.findAll();

		model.addAttribute("customers", customers);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	@PostMapping("/order")
	public String order(@RequestParam("customerId") Long customerId,
				@RequestParam("orderItemQuantity") int orderItemQuantity,
				@RequestParam("itemId") Long itemId) {
		
		orderService.order(customerId, itemId, orderItemQuantity);
		
		return "redirect:/orders";
	}
	
	@GetMapping("/orders")
	public String orderList(Model model) {
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		
		return "order/orderList";
	}
	
	@RequestMapping("/order/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
	
}
