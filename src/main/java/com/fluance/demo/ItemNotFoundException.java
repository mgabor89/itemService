package com.fluance.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -7946770981659967075L;

	public ItemNotFoundException(Long id) {
        super("Item not found for ID: " + id);
    }
}
