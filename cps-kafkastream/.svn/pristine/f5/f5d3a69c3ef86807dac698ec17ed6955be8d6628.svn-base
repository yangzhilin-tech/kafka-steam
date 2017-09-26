package com.wgmf.web;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.WriteResult;

@Component
public class Receiver {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private Client esClient;

	Gson gson = new GsonBuilder().create();

	private static final Logger LOGGER = LogManager.getLogger(Receiver.class);

	public void doTaskOne(String content) {
		Mongoson son = gson.fromJson(content, Mongoson.class);
		Query query = new Query();
		query.addCriteria(Criteria.where("key").is(son.getKey()));
		Update update = new Update().set("result", son.getResult()).set(
				"timestamp", son.getTimestamp());
		WriteResult wr = mongoTemplate.upsert(query, update,
				CpsKafkastreamApplication.SUM_MONGODB_TOPIC);


	/*	IndexRequest indexRequest = new IndexRequest("nginxindex",
				"nginxindextype", son.getKey()).source(gson.toJson(son));

		UpdateRequest updateRequest = new UpdateRequest("nginxindex",
				"nginxindextype", son.getKey()).doc(gson.toJson(son)).upsert(
				indexRequest);

		try {
			esClient.update(updateRequest).get();
		} catch (Exception e) {
			LOGGER.error(e);
		}*/

	}

	/*
	 * public void doTask(String content) { // System.out.println("receive:" +
	 * content);
	 * 
	 * if (content.indexOf("下单") >= 0) { LocalDateTime lo =
	 * LocalDateTime.now(ZoneId.systemDefault()); lo = lo.minusHours(8);
	 * DateTimeFormatter formatter = DateTimeFormatter
	 * .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"); String date =
	 * lo.format(formatter); // System.out.println(lo);
	 * mongoTemplate.insert(content.replaceFirst(", \\{", "{"), "wgpoint");
	 * String replaceFirst = content.replaceFirst(", \\{", "{\"@timestamp\":\""
	 * + date + "\"" + ",");
	 * 
	 * }
	 * 
	 * }
	 */

	@KafkaListener(topics = CpsKafkastreamApplication.SUM_MONGODB_TOPIC)
	public void processMessage4apptopic(String content) {
		doTaskOne(content);
	}

}
