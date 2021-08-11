package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Component
public class TwitterKafkaStreamRunner implements StreamRunner{

	private static final Logger Log=LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);
	private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
	private final TwitterKafkaStatusListener twitterKafkaStatusListener;
	
	private TwitterStream twitterStream;
	
	public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,TwitterKafkaStatusListener twitterKafkaStatusListener) {
		this.twitterKafkaStatusListener = twitterKafkaStatusListener;
		this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
	}
	
	
	@Override
	public void start() throws TwitterException {
		twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(twitterKafkaStatusListener);
		addFilter();
	}
	
	@PreDestroy
	public void shutdown() {
		if(twitterStream != null) {
			Log.info("closing twitter stream");
			twitterStream.shutdown();
		}
	}
	
	private void addFilter() {
		String[] keyWords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
		FilterQuery filterQuery = new FilterQuery(keyWords);
		twitterStream.filter(filterQuery);
		Log.info("started filtering keywords" , Arrays.toString(keyWords) );
	}

}
