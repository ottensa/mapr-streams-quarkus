package de.ottensa.mapres;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class ConsumerApplication {

	@Inject
	@ConfigProperty(name="mapr.topic")
	private String topic;

    void onStart(@Observes StartupEvent ev) {
		System.out.println("PostConstruct");
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.submit(new Runnable(){
		
			@Override
			public void run() {

				Properties props = new Properties();
				props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
				props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
				props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
				
				final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
				consumer.subscribe(Arrays.asList(topic));
				ConsumerRecords<String, String> records = consumer.poll(0);
				System.out.println(records.count());

				while (true) {
					records = consumer.poll(1000);
					for (ConsumerRecord<String, String> record: records) {
						System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					}
				}
			}
		});
	}
}