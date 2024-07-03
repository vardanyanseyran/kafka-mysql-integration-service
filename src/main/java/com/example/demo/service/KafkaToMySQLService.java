package com.example.demo.service;

import com.example.demo.component.DatabaseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

@Component
public class KafkaToMySQLService implements CommandLineRunner {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    private DatabaseUtil databaseUtil;

    private static final String TOPIC = "some_topics";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void run(String... args) {
        kafkaConsumer.subscribe(Collections.singletonList(TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    JsonNode jsonNode = OBJECT_MAPPER.readTree(record.value());
                    String messageContent = jsonNode.get("message").asText();
                    databaseUtil.saveMessage(messageContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}