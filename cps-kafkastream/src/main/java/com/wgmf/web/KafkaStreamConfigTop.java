package com.wgmf.web;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.wgmf.web.CpsKafkastreamApplication.CustomRocksDBConfig;
                       
@Component
@Configuration
@ConfigurationProperties(prefix = "kafkastreamtop")
public class KafkaStreamConfigTop {

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

	@Bean("streamstop")
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

		Serde<String> stringSerde = Serdes.String();

		Serde<Long> longSerde = Serdes.Long();

		final Serde<Windowed<String>> windowedStringSerde = new WindowedSerde<>(
				stringSerde);

		TimeWindows advanceBy = TimeWindows.of(3 * 60 * 1000L).advanceBy(
				3 * 60 * 1000L);
		// https://docs.confluent.io/current/streams/developer-guide.html#streams-developer-guide-dsl-aggregating

		builder.stream(stringSerde, longSerde,
				CpsKafkastreamApplication.SUM_OF_ODD_NUMBERS_TOPIC)
				.map((k, v) -> {
					List<String> msgs = Arrays.asList(k.split(":"));
					return new KeyValue<>(msgs.get(0), msgs.get(2) + ":" + v);
				})

				.selectKey((k, v) -> {
					return k;
				})

				.groupByKey(stringSerde, stringSerde)
				.aggregate(
						() -> "Initializer:0;",
						(k, v, nrs) -> {
							nrs = nrs + v;

							String[] stringsort = nrs.split(";");

							Arrays.sort(stringsort, (a, b) -> {

								if (a == null) {
									return 1;
								}

								if (b == null) {
									return -1;
								}
								return Long.compare(Long.valueOf(Arrays.asList(
										b.split(":")).get(1)), Long
										.valueOf(Arrays.asList(a.split(":"))
												.get(1)));
							});

							if (stringsort.length > 3) {
								nrs = "";

								for (int i = 0; i < 3; i++) {

									if (i == 0) {
										nrs += stringsort[i] + ";";
									}

									if (i >= 1
											&& !(stringsort[i].split(":")[0])
													.equals(stringsort[i - 1]
															.split(":")[0])) {

										nrs += stringsort[i] + ";";

									}

								}

								return nrs;
							} else {
								return nrs + ";";

							}

						}, advanceBy, stringSerde, "aggregatestore")
				.through(windowedStringSerde, stringSerde, "aggregatetopic")
				.toStream().map((k, v) -> {

					// windowedStringSerde.deserializer().deserialize("aggregatetopic",
						// k.key().getBytes()).window().;

						return new KeyValue<>(k.key(), v);
					}).to(stringSerde, stringSerde, "aggregatetopic4string");

		// .to(windowedStringSerde, stringSerde, "aggregatetopic");

		KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
		// https://stackoverflow.com/questions/41394065/kafka-streams-0-10-1-failed-to-flush-state-store
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
