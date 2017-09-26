package com.wgmf.web.service;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import com.wgmf.web.AppContextHolder;
import com.wgmf.web.CpsKafkastreamApplication;

public class StreamService {

	private KafkaStreams kstream;

	public StreamService(KafkaStreams kstream) {
		super();
		this.kstream = kstream;
	}

	public String getValueBykey(String key) {

		ReadOnlyKeyValueStore<String, Long> keyValueStore = kstream.store(
				CpsKafkastreamApplication.SUM_OF_ODD_NUMBERS_TOPIC,
				QueryableStoreTypes.keyValueStore());

		return keyValueStore.get(key) + "";
	}

}
