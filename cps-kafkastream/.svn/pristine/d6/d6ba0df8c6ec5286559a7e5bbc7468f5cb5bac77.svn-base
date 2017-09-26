package com.wgmf.web;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wgmf.web.CpsKafkastreamApplication.CustomRocksDBConfig;

@Component
@Configuration
@ConfigurationProperties(prefix = "kafkastream")
public class KafkaStreamConfig {

	private static final Logger LOGGER = LogManager
			.getLogger(CpsKafkastreamApplication.class);

	private String bootstrapServers;

	private String clientid;

	private String applicationid;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	@Bean("streams")
	public KafkaStreams getStreams() {
		Properties streamsConfiguration = new Properties();
		streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,
				this.applicationid);
		streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, this.clientid);
		streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				this.bootstrapServers);
		streamsConfiguration.put(
				StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,
				60 * 1024 * 1024L);
		streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass().getName());
		streamsConfiguration.put(
				StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()
						.getClass().getName());

		streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
				"latest");

		streamsConfiguration.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 800);

		streamsConfiguration.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
				160000);

		streamsConfiguration.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,
				180000);

		streamsConfiguration.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
				180000);

		streamsConfiguration.put(ConsumerConfig.CHECK_CRCS_CONFIG, false);
		streamsConfiguration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				Serdes.String().getClass().getName());
		streamsConfiguration.put(
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serdes.String()
						.getClass().getName());

		streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG,
				"/tmp/kafka-stream11");
		streamsConfiguration.put(
				StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG,
				CustomRocksDBConfig.class);

		streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,
				10 * 1000);
		streamsConfiguration.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 10);

		streamsConfiguration.put(
				StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				WallclockTimestampExtractor.class);

		KStreamBuilder builder = new KStreamBuilder();

		KStreamBuilder buildertop = new KStreamBuilder();
		// LOGGER.info("333");
		Pattern pattern = Pattern.compile("\\W+",
				Pattern.UNICODE_CHARACTER_CLASS);

		Serde<String> stringSerde = Serdes.String();
		Serde<Long> longSerde = Serdes.Long();

		Gson gson = new GsonBuilder().create();

		builder.stream(stringSerde, stringSerde,
				CpsKafkastreamApplication.NUMBERS_TOPIC)
				.mapValues(v -> {
					return gson.fromJson(v, Message.class).getMessage();
				})
				.selectKey(
						(k, v) -> {
							String msg = v.replaceAll("\\-", "");
							msg = msg.replaceAll("\"", "")
									.replaceAll("  ", " ");
							List<String> msgs = Arrays.asList(msg.split(" "));
							return msgs.get(0) + ":" + msgs.get(3) + ":"
									+ msgs.get(8) + ":"
									+ msgs.get(1).substring(1, 12);
						})
				.groupByKey(stringSerde, stringSerde)
				.count(CpsKafkastreamApplication.SUM_OF_ODD_NUMBERS_TOPIC)
				.through(stringSerde, longSerde,
						CpsKafkastreamApplication.SUM_OF_ODD_NUMBERS_TOPIC)
				.toStream()
				.map((k, v) -> {

					Mongoson son = new Mongoson();
					son.setKey(k);
					son.setResult(v.longValue());

					return new KeyValue<>(k.toUpperCase(), gson.toJson(son));

				})
				.to(stringSerde, stringSerde,
						CpsKafkastreamApplication.SUM_MONGODB_TOPIC);

		
		KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);

		streams.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				LOGGER.info(arg1);
			}
		});

		streams.cleanUp();
		streams.start();

		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

		return streams;
	}

}
