package com.example.demo.query.api.projection;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.command.api.data.Product;
import com.example.demo.command.api.data.ProductRepository;
import com.example.demo.command.api.model.ProductRestModel;
import com.example.demo.query.api.queries.GetProductsQuery;

@Component
public class ProductProjection {

	@Autowired
	private ProductRepository productRepository;
	
	@QueryHandler
	public List<ProductRestModel> handle(GetProductsQuery getProductsQuery){
		List<Product> products = productRepository.findAll();
		
		List<ProductRestModel> productRestModels = products.stream()
				.map(product -> ProductRestModel
						.builder()
						.quantity(product.getQuantity())
						.price(product.getPrice())
						.name(product.getName())
						.build())
				.collect(Collectors.toList());
		
		return productRestModels;
	}
}
