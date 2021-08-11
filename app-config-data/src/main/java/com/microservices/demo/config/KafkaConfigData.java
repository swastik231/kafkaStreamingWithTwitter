package com.microservices.demo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {
	private String bootstrapServer;
	private String schemaRegistryUrlKey;
	private String schemaRegistryUrl;
	private String topicName;;
	private List<String> topicNameToCreate;
	private Integer numOfPartitions;
	private Short replicationFactor;

	public String getBootstrapServer() {
		return bootstrapServer;
	}

	public void setBootstrapServer(String bootstrapServer) {
		this.bootstrapServer = bootstrapServer;
	}

	public String getSchemaRegistryUrlKey() {
		return schemaRegistryUrlKey;
	}

	public void setSchemaRegistryUrlKey(String schemaRegistryUrlKey) {
		this.schemaRegistryUrlKey = schemaRegistryUrlKey;
	}

	public String getSchemaRegistryUrl() {
		return schemaRegistryUrl;
	}

	public void setSchemaRegistryUrl(String schemaRegistryUrl) {
		this.schemaRegistryUrl = schemaRegistryUrl;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public List<String> getTopicNameToCreate() {
		return topicNameToCreate;
	}

	public void setTopicNameToCreate(List<String> topicNameToCreate) {
		this.topicNameToCreate = topicNameToCreate;
	}

	public Integer getNumOfPartitions() {
		return numOfPartitions;
	}

	public void setNumOfPartitions(Integer numOfPartitions) {
		this.numOfPartitions = numOfPartitions;
	}

	public Short getReplicationFactor() {
		return replicationFactor;
	}

	public void setReplicationFactor(Short replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

}
