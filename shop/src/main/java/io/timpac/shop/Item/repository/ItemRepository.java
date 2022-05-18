package io.timpac.shop.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.timpac.shop.Item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
		
}
