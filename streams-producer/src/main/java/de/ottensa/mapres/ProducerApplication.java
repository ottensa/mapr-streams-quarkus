package de.ottensa.mapres;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class ProducerApplication {

	@Inject
	ProducerService producerService;

	@Scheduled(every = "1s")
    void scheduledMessage() {
		UUID uuid = UUID.randomUUID();
        producerService.send(uuid.toString());
		System.out.println("Sent: " + uuid.toString());
    }

}