package com.amdocs.chainstore.kafka;

import static com.amdocs.chainstore.kafka.KafkaTopics.*;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneStoreProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void publishToGetTopic(Object message) {
    kafkaTemplate.send(PHONE_INVENTORY_GET, message);
  }
  public void publishToDeleteTopic(Object message) {
    kafkaTemplate.send(PHONE_INVENTORY_DELETE, message);
  }
  public void publishToUpdateTopic(Object message) {
    kafkaTemplate.send(PHONE_INVENTORY_UPDATE, message);
  }
}
