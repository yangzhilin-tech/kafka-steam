package com.wgmf.web;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wgmf.web.CpsKafkastreamApplication.CustomRocksDBConfig;

@Component
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfig {
	private String clusterName;

	private String clusterNodes;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	@Bean("esClient")
	public Client getESClient() {
		// 设置集群名字
		Settings settings = Settings.builder()
				.put("cluster.name", this.clusterName).build();
		Client client = new PreBuiltTransportClient(settings);

		try {
			// 读取的ip列表是以逗号分隔的
			for (String clusterNode : this.clusterNodes.split(",")) {
				String ip = clusterNode.split(":")[0];
				String port = clusterNode.split(":")[1];
				((TransportClient) client)
						.addTransportAddress(new InetSocketTransportAddress(
								InetAddress.getByName(ip), Integer
										.parseInt(port)));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return client;
	}



}
