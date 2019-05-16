package com.fluance.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;

    @PostMapping(value = "/store")
    public Item store(@RequestBody Item item) {
        return itemService.store(item);
    }

    @GetMapping(value = "/getAll")
    public List<Item> getAll() {
        return itemService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Item getById(@PathVariable("id") long itemId) {
        return itemService.findById(itemId);
    }
}
