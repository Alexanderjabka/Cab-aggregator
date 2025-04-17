package org.internship.task.ratingservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final String TOPIC = "ride-completed-events";

    private final RatingRepository ratingRepository;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @Autowired
    public KafkaConsumerService(RatingRepository ratingRepository,
                                ObjectMapper objectMapper,
                                Tracer tracer) {
        this.ratingRepository = ratingRepository;
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }

    @KafkaListener(topics = TOPIC, groupId = "rating-group")
    public void consumeRideCompletedEvent(ConsumerRecord<String, String> record) throws JsonProcessingException {
        Span span = tracer.nextSpan()
            .name("kafka-consume")
            .tag("kafka.topic", TOPIC)
            .tag("kafka.partition", String.valueOf(record.partition()))
            .tag("kafka.offset", String.valueOf(record.offset()));

        try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
            System.out.println("Received message: " + record.value());
            RideCompletedEvent event = objectMapper.readValue(record.value(), RideCompletedEvent.class);

            if (!ratingRepository.existsByRideId(event.getRideId())) {
                Rating rating = new Rating();
                rating.setRideId(event.getRideId());
                rating.setPassengerId(event.getPassengerId());
                rating.setDriverId(event.getDriverId());
                rating.setIsDeleted(false);
                ratingRepository.save(rating);
            }
        } finally {
            span.end();
        }
    }
}