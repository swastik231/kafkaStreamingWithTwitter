package com.microservices.demo.common.client;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.message.CreateTopicsResponseData.CreatableTopicResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservices.demo.common.exception.KafkaClientException;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.RetryConfigData;

@Component
public class KafkaAdminClient {
	private static final Logger log = LoggerFactory.getLogger(KafkaAdminClient.class);
	private final KafkaConfigData kafkaConfigData;
	private final RetryConfigData retryConfigData;
	private final AdminClient adminClient;
	private final RetryTemplate retryTemplate;
	
	
	@Autowired
	private WebClient webClient;

	public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, AdminClient adminClient,
			RetryTemplate retryTemplate) {
		this.kafkaConfigData = kafkaConfigData;
		this.retryConfigData = retryConfigData;
		this.adminClient = adminClient;
		this.retryTemplate = retryTemplate;
	}

	public void createTopics() {
		CreateTopicsResult createTopicsResult;
		try {
			createTopicsResult = retryTemplate.execute(this::doCreateTopics);
		} catch (Throwable t) {
			throw new KafkaClientException("Reached maximum number of retry", t);
		}
		checkTopicsCreated();
	}

	private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
		List<String> topicnames = kafkaConfigData.getTopicNameToCreate();
		log.info("createing {} topics , attempt{}", topicnames.size(), retryContext.getRetryCount());
		List<NewTopic> kafkaTopics = topicnames.stream().map(topic -> new NewTopic(topic.trim(),
				kafkaConfigData.getNumOfPartitions(), kafkaConfigData.getReplicationFactor()))
				.collect(Collectors.toList());

		return adminClient.createTopics(kafkaTopics);
	}

	public void checkTopicsCreated() {
		Collection<TopicListing> topics = getTopics();
		int retryCount = 1;
		Integer maxRetry = retryConfigData.getMaxAttempts();
		Integer multiplier = retryConfigData.getMultiplier().intValue();
		Long sleepTimeMs = retryConfigData.getSleepTimeMs();
		for (String topic : kafkaConfigData.getTopicNameToCreate()) {
			while (!isTopicsCreated(topics, topic)) {
				checkMaxRetry(retryCount++, maxRetry);
				sleep(sleepTimeMs);
				sleepTimeMs *= multiplier;
				topics = getTopics();
			}
		}

	}

	public void checkSchemaRegistry() {
		int retryCount = 1;
		Integer maxRetry = retryConfigData.getMaxAttempts();
		Integer multiplier = retryConfigData.getMultiplier().intValue();
		Long sleepTimeMs = retryConfigData.getSleepTimeMs();
		while (!getSchemaRegistryStatus().is2xxSuccessful()) {
			checkMaxRetry(retryCount++, maxRetry);
			sleep(sleepTimeMs);
			sleepTimeMs *= multiplier;
		}
	}

	private HttpStatus getSchemaRegistryStatus() {
		try {
			return webClient.method(HttpMethod.GET).uri(kafkaConfigData.getSchemaRegistryUrl()).exchange()
					.map(ClientResponse::statusCode).block();
		} catch (Exception e) {
			return HttpStatus.SERVICE_UNAVAILABLE;
		}

	}

	private void sleep(Long sleepTimeMs) {
		try {
			Thread.sleep(sleepTimeMs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkMaxRetry(int retry, Integer maxRetry) {
		if (retry > maxRetry)
			throw new KafkaClientException("Reached maximum number of retry to read kafka topics");

	}

	private boolean isTopicsCreated(Collection<TopicListing> topics, String topicName) {
		// TODO Auto-generated method stub
		if (topics == null)
			return false;
		return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
	}

	private Collection<TopicListing> getTopics() {
		Collection<TopicListing> topics;

		try {
			topics = retryTemplate.execute(this::doGetTopics);
		} catch (Throwable t) {
			// TODO Auto-generated catch block
			throw new KafkaClientException("Reached maximum number of retry", t);
		}
		return topics;
	}

	private Collection<TopicListing> doGetTopics(RetryContext retryContext) {
		log.info("reading kafka topics {}, attempt{}", kafkaConfigData.getTopicNameToCreate().toArray(),
				retryContext.getRetryCount());
		Collection<TopicListing> topics = null;
		try {
			topics = adminClient.listTopics().listings().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (topics != null) {
			topics.forEach(topic -> log.debug("topics with name {}", topic.name()));
		}

		return topics;
	}
}
