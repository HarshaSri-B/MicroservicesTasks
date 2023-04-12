package com.example.demo.command.api.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@TargetAggregateIdentifier
public class CreateProductCommand {
	
	
	@TargetAggregateIdentifier
	private String productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
}
