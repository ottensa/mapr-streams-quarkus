package de.ottensa.mapres;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ProducerService {

	@Inject
	@ConfigProperty(name="mapr.topic")
	private String topic;
	
	private KafkaProducer<String, String> producer;

	@PostConstruct
    void setup() {
		Properties props = new Properties();
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
	}

	public void send(String msg) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
		producer.send(record);
	}

	@PreDestroy
	void cleanup() {
		producer.close();
	}
}