package org.internship.task.rideservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Tracer;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final String TOPIC = "ride-completed-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper,
                                Tracer tracer) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }

    public void sendRideCompletedEvent(RideCompletedEvent event) throws JsonProcessingException {
        try {
            String message = objectMapper.writeValueAsString(event);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
            record.headers().add("traceId",
                tracer.currentSpan().context().traceId().getBytes(StandardCharsets.UTF_8));

            kafkaTemplate.send(record);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
