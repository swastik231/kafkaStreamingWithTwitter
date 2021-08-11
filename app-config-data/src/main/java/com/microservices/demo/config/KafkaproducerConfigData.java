package com.microservices.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaproducerConfigData {
	private String keySerializerClass;
	private String valueSerializerClass;
	private String compressionType;
	private String ack;
	private Integer batchSize;
	private Integer batchSizeBoostFactor;
	private Integer lingerMs;
	private Integer requestTimeoutMs;
	private Integer retryCount;

	public String getKeySerializerClass() {
		return keySerializerClass;
	}

	public void setKeySerializerClass(String keySerializerClass) {
		this.keySerializerClass = keySerializerClass;
	}

	public String getValueSerializerClass() {
		return valueSerializerClass;
	}

	public void setValueSerializerClass(String valueSerializerClass) {
		this.valueSerializerClass = valueSerializerClass;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public Integer getBatchSizeBoostFactor() {
		return batchSizeBoostFactor;
	}

	public void setBatchSizeBoostFactor(Integer batchSizeBoostFactor) {
		this.batchSizeBoostFactor = batchSizeBoostFactor;
	}

	public Integer getLingerMs() {
		return lingerMs;
	}

	public void setLingerMs(Integer lingerMs) {
		this.lingerMs = lingerMs;
	}

	public Integer getRequestTimeoutMs() {
		return requestTimeoutMs;
	}

	public void setRequestTimeoutMs(Integer requestTimeoutMs) {
		this.requestTimeoutMs = requestTimeoutMs;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

}
