package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	@Autowired
	private Environment environment;

	@Autowired
	private CurrencyExchangeRepository repo;

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retriveExchangeValue(@PathVariable String from, @PathVariable String to) {

		CurrencyExchange ce = repo.findByFromAndTo(from, to);

		if (ce == null) {
			throw new RuntimeException("Unable to find data for " + from + " to " + to);
		}

		// CurrencyExchange ce = new CurrencyExchange(1000L, from, to,
		// BigDecimal.valueOf(50));
		String port = environment.getProperty("local.server.port");
		ce.setEnvironment(port);
		return ce;
	}

}
