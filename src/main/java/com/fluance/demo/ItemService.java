package com.fluance.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepositoy;

    private final ItemApplicationConfiguration config;

    public Item store(Item item) {
        final int numberOfAllowedItems = config.getMaximumNumber();
        if (itemRepositoy.count() < numberOfAllowedItems) {
            return itemRepositoy.save(item);
        }
        throw new TooManyItemsException(numberOfAllowedItems);
    }

    Item findById(Long itemId) {
        final Optional<Item> byId = itemRepositoy.findById(itemId);
        return byId.orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    List<Item> getAll() {
        return itemRepositoy.findAll();
    }
}
