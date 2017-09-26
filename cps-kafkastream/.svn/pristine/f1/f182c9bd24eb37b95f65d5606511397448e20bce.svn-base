package com.wgmf.web.controller;

import java.util.Collection;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wgmf.web.AppContextHolder;
import com.wgmf.web.CpsKafkastreamApplication;

@RestController
public class StreamController {
	
	@Autowired
	private KafkaStreams streams;

	@RequestMapping(value = "/getValueBykey", method = RequestMethod.GET)
	public String getValueBykey(String key) {
		//KafkaStreams streams = CpsKafkastreamApplication.hm.get("stream");

		//Collection<StreamsMetadata> cms= streams.allMetadata();
		
		
		ReadOnlyKeyValueStore<String, Long> keyValueStore = streams.store(
				CpsKafkastreamApplication.SUM_OF_ODD_NUMBERS_TOPIC,
				QueryableStoreTypes.keyValueStore());
		
		
		return keyValueStore.get(key) + "";
	}

}
