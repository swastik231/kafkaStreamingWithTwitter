package com.microservices.demo.twitter.to.kafka.service.init;

import org.springframework.stereotype.Component;

@Component
public interface StreamInitializer {
	void init();
}
