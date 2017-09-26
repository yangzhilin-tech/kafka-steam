package com.wgmf.web;

import java.util.Map;

import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.Options;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class CpsKafkastreamApplication {
	private static final Logger LOGGER = LogManager
			.getLogger(CpsKafkastreamApplication.class);

	public static final String SUM_OF_ODD_NUMBERS_TOPIC = "ip-result-topic11";
	static final String NUMBERS_TOPIC = "iptopic11";
	static final String SUM_MONGODB_TOPIC = "mongodb-topic11";

	public static class CustomRocksDBConfig implements RocksDBConfigSetter {

		@Override
		public void setConfig(final String storeName, final Options options,
				final Map<String, Object> configs) {
			BlockBasedTableConfig tableConfig = new org.rocksdb.BlockBasedTableConfig();
			tableConfig.setBlockCacheSize(100 * 1024 * 1024L);
			tableConfig.setBlockSize(64 * 1024L);
			tableConfig.setCacheIndexAndFilterBlocks(true);
			options.setTableFormatConfig(tableConfig);
			options.setWriteBufferSize(32 * 1024 * 1024L);
			options.setMaxWriteBufferNumber(3);
			int compactionParallelism = Math.max(Runtime.getRuntime()
					.availableProcessors(), 2);
			options.setIncreaseParallelism(compactionParallelism);
		}
	}

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(
				CpsKafkastreamApplication.class);

		application.addListeners(new ApplicationListenerFailed());

		application.addListeners(new ApplicationListenerStarted());
		application.run(args);

	}

	


}
