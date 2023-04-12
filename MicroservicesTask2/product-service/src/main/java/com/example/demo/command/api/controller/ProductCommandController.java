package com.example.demo.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.command.api.commands.CreateProductCommand;
import com.example.demo.command.api.model.ProductRestModel;


@RestController
@RequestMapping("/products")
public class ProductCommandController {
	
	 //@Autowired
	 private CommandGateway commandGateway;
	 
	
	public ProductCommandController(@Lazy CommandGateway commandGateway) {
		
		this.commandGateway = commandGateway;
	}


	@PostMapping
	public String addProduct(@RequestBody ProductRestModel model) {
		
		CreateProductCommand  createProductCommand = CreateProductCommand.builder()
													 .productId(UUID.randomUUID().toString())
													 .name(model.getName())
													 .price(model.getPrice())
													 .quantity(model.getQuantity())
													 .build();
	
		String result = commandGateway.sendAndWait(createProductCommand);
		return result;
	}

}
