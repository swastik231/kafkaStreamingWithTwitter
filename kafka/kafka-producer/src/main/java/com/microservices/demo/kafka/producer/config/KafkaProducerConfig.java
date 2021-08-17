package com.microservices.demo.kafka.producer.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaproducerConfigData;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

	private final KafkaConfigData kafkaConfigData;
	private final KafkaproducerConfigData kafkaproducerConfigData;

	public KafkaProducerConfig(KafkaConfigData kafkaConfigData, KafkaproducerConfigData kafkaproducerConfigData) {
		super();
		this.kafkaConfigData = kafkaConfigData;
		this.kafkaproducerConfigData = kafkaproducerConfigData;
	}
	
	@Bean
	public Map<String, Object> producerConfig(){
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
		props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaproducerConfigData.getKeySerializerClass());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaproducerConfigData.getValueSerializerClass());
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaproducerConfigData.getBatchSize()*
				kafkaproducerConfigData.getBatchSizeBoostFactor());
		props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaproducerConfigData.getLingerMs());
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaproducerConfigData.getCompressionType());
		props.put(ProducerConfig.ACKS_CONFIG, kafkaproducerConfigData.getAck());
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaproducerConfigData.getRequestTimeoutMs());
		props.put(ProducerConfig.RETRIES_CONFIG, kafkaproducerConfigData.getRetryCount());
		return props;
	}
	
	@Bean
	public ProducerFactory<K, V> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerConfig());
	}
	
	@Bean
	public KafkaTemplate<K, V> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
}
