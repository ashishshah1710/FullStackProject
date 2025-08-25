package com.phonestore.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonestore.model.PhoneInventoryItem;
import com.phonestore.model.PhoneInventoryItemRequest;
import com.phonestore.service.PhoneInventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryKafkaConsumer {

  private final PhoneInventoryService phoneInventoryService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = KafkaTopics.PHONE_INVENTORY_GET, groupId = KafkaTopics.GROUP_ID)
  public List<PhoneInventoryItem> handleGetTopic(PhoneInventoryEvent phoneInventoryEvent) {
    log.info("Received GET topic message - Operation: {}, Event: {}",
        phoneInventoryEvent.getAction(), phoneInventoryEvent);

    try {
      List<PhoneInventoryItem> result;
      switch (phoneInventoryEvent.getAction()) {
        case "GET_PHONE_BY_ID":
          log.debug("Processing GET_PHONE_BY_ID with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> getByIdPayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String phoneId = (String) getByIdPayloadMap.get("phoneId");

            if (phoneId == null || phoneId.trim().isEmpty()) {
              log.warn("Invalid phone ID in GET_PHONE_BY_ID payload: {}", phoneId);
              return List.of();
            }

            log.debug("Getting phone by ID: {}", phoneId);

            PhoneInventoryItem phone = phoneInventoryService.getById(phoneId);
            result = phone != null ? List.of(phone) : List.of();

            log.info("Successfully processed GET_PHONE_BY_ID for phoneId: {}, found: {}", phoneId, phone != null);
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing GET_PHONE_BY_ID payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for GET_PHONE_BY_ID operation", e);
          } catch (Exception e) {
            log.error("Error processing GET_PHONE_BY_ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_PHONE_BY_ID operation", e);
          }

        case "GET_PHONES_BY_IDS":
          log.debug("Processing GET_PHONES_BY_IDS with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> getByIdsPayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            @SuppressWarnings("unchecked")
            List<String> phoneIds = (List<String>) getByIdsPayloadMap.get("phoneIds");

            if (phoneIds == null || phoneIds.isEmpty()) {
              log.warn("No phone IDs found in GET_PHONES_BY_IDS payload");
              return List.of();
            }

            // Clean up the phoneIds in case they have "ids=" prefix
            List<String> cleanPhoneIds = phoneIds.stream()
                .map(id -> id.startsWith("ids=") ? id.substring(4) : id)
                .collect(java.util.stream.Collectors.toList());

            log.debug("Getting {} phones by IDs", cleanPhoneIds.size());

            result = phoneInventoryService.getByIds(cleanPhoneIds);

            log.info("Successfully processed GET_PHONES_BY_IDS for {} phone IDs, found: {}",
                cleanPhoneIds.size(), result.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing GET_PHONES_BY_IDS payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for GET_PHONES_BY_IDS operation", e);
          } catch (Exception e) {
            log.error("Error processing GET_PHONES_BY_IDS: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_PHONES_BY_IDS operation", e);
          }

        case "GET_PHONES_BY_MODEL":
          log.debug("Processing GET_PHONES_BY_MODEL with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> getByModelPayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String model = (String) getByModelPayloadMap.get("model");

            if (model == null || model.trim().isEmpty()) {
              log.warn("Invalid model in GET_PHONES_BY_MODEL payload: {}", model);
              return List.of();
            }

            log.debug("Getting phones by model: {}", model);

            result = phoneInventoryService.getByModel(model);

            log.info("Successfully processed GET_PHONES_BY_MODEL for model: {}, found: {}", model, result.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing GET_PHONES_BY_MODEL payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for GET_PHONES_BY_MODEL operation", e);
          } catch (Exception e) {
            log.error("Error processing GET_PHONES_BY_MODEL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_PHONES_BY_MODEL operation", e);
          }

        case "GET_PHONES_BY_MODELS":
          log.debug("Processing GET_PHONES_BY_MODELS with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> getByModelsPayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            @SuppressWarnings("unchecked")
            List<String> models = (List<String>) getByModelsPayloadMap.get("models");

            if (models == null || models.isEmpty()) {
              log.warn("No models found in GET_PHONES_BY_MODELS payload");
              return List.of();
            }

            log.debug("Getting phones by {} models", models.size());

            result = phoneInventoryService.getByModels(models);

            log.info("Successfully processed GET_PHONES_BY_MODELS for {} models, found: {}",
                models.size(), result.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing GET_PHONES_BY_MODELS payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for GET_PHONES_BY_MODELS operation", e);
          } catch (Exception e) {
            log.error("Error processing GET_PHONES_BY_MODELS: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_PHONES_BY_MODELS operation", e);
          }

        case "GET_ALL_PHONES":
          log.debug("Processing GET_ALL_PHONES with payload: {}", phoneInventoryEvent.getPayload());

          try {
            log.debug("Getting all phones");

            result = phoneInventoryService.getAll();

            log.info("Successfully processed GET_ALL_PHONES, found: {}", result.size());
            return result;

          } catch (Exception e) {
            log.error("Error processing GET_ALL_PHONES: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_ALL_PHONES operation", e);
          }
        case "GET_PHONES_BY_STORE_ID":
          log.debug("Processing GET_PHONES_BY_STORE_ID with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> getByStoreIdPayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String storeId = (String) getByStoreIdPayloadMap.get("storeId");

            if (storeId == null || storeId.trim().isEmpty()) {
              log.warn("Invalid store ID in GET_PHONES_BY_STORE_ID payload: {}", storeId);
              return List.of();
            }

            log.debug("Getting phones by store ID: {}", storeId);

            result = phoneInventoryService.getPhonesByStoreId(storeId);

            log.info("Successfully processed GET_PHONES_BY_STORE_ID for storeId: {}, found: {}", storeId, result.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing GET_PHONES_BY_STORE_ID payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for GET_PHONES_BY_STORE_ID operation", e);
          } catch (Exception e) {
            log.error("Error processing GET_PHONES_BY_STORE_ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process GET_PHONES_BY_STORE_ID operation", e);
          }
        default:
          log.warn("Unknown operation for GET topic: {}, Event: {}",
              phoneInventoryEvent.getAction(), phoneInventoryEvent);
          return List.of();
      }
    } catch (Exception e) {
      log.error("Error handling GET topic - Operation: {}, Event: {}, Error: {}",
          phoneInventoryEvent.getAction(), phoneInventoryEvent, e.getMessage(), e);
      return List.of();
    }
  }

  @KafkaListener(topics = KafkaTopics.PHONE_INVENTORY_UPDATE , groupId = KafkaTopics.GROUP_ID)
  public List<PhoneInventoryItem> handleUpdateTopic(PhoneInventoryEvent phoneInventoryEvent){
    log.info("Received UPDATE topic message - Operation: {}, Event: {}",
        phoneInventoryEvent.getAction(), phoneInventoryEvent);

    try {
      List<PhoneInventoryItem> result;
      switch (phoneInventoryEvent.getAction()) {
        case "ADD_PHONES":
          log.debug("Processing ADD_PHONES with phonesToAdd: {}", phoneInventoryEvent.getPayload());

          try {
            // Convert the payload (List of LinkedHashMaps) to List of PhoneInventoryItemRequest
            List<PhoneInventoryItemRequest> phoneRequests = objectMapper.convertValue(
                phoneInventoryEvent.getPayload(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PhoneInventoryItemRequest.class)
            );

            log.debug("Successfully converted payload to {} phone requests", phoneRequests.size());
            result = phoneInventoryService.addPhones(phoneRequests,phoneInventoryEvent.getStoreId());
            log.info("Successfully processed ADD_PHONES, added {} phones", result.size());
            return result;
          } catch (IllegalArgumentException e) {
            log.error("Error converting payload to PhoneInventoryItemRequest: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload format for ADD_PHONES operation", e);
          }

        case "ORDER_PHONES":
          log.debug("Processing ORDER_PHONES with payload: {}", phoneInventoryEvent.getPayload());

          try {
            // Convert the payload to extract orders
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> payloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            @SuppressWarnings("unchecked")
            List<java.util.Map<String, Object>> orders = (List<java.util.Map<String, Object>>) payloadMap.get("orders");

            if (orders == null || orders.isEmpty()) {
              log.warn("No orders found in ORDER_PHONES payload");
              return List.of();
            }

            log.debug("Processing {} orders", orders.size());

            // Process each order to decrease inventory
            for (java.util.Map<String, Object> order : orders) {
              String phoneId = (String) order.get("phoneId");
              Integer quantity = (Integer) order.get("quantity");
              String orderStoreId = (String) order.get("storeId");

              if (phoneId == null || quantity == null || orderStoreId == null) {
                log.warn("Invalid order data - phoneId: {}, quantity: {}, storeId: {}",
                    phoneId, quantity, orderStoreId);
                continue;
              }

              log.debug("Processing order - phoneId: {}, quantity: {}, storeId: {}",
                  phoneId, quantity, orderStoreId);

              // Decrease phone inventory
              phoneInventoryService.decreaseQuantity(phoneId, quantity, orderStoreId);
            }

            // Return updated inventory for the store
            result = phoneInventoryService.getPhonesByStoreId(
                orders.get(0).get("storeId").toString());

            log.info("Successfully processed ORDER_PHONES for {} orders", orders.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing ORDER_PHONES payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for ORDER_PHONES operation", e);
          } catch (Exception e) {
            log.error("Error processing ORDER_PHONES: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process ORDER_PHONES operation", e);
          }

        case "INCREASE_QUANTITY":
          log.debug("Processing INCREASE_QUANTITY with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> increasePayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String phoneId = (String) increasePayloadMap.get("phoneId");
            Integer quantity = (Integer) increasePayloadMap.get("quantity");

            if (phoneId == null || quantity == null) {
              log.warn("Invalid INCREASE_QUANTITY data - phoneId: {}, quantity: {}", phoneId, quantity);
              return List.of();
            }

            log.debug("Increasing quantity - phoneId: {}, quantity: {}", phoneId, quantity);

            // Increase phone inventory
            phoneInventoryService.increaseQuantity(phoneId, quantity);

            // Return updated inventory for the store
            result = phoneInventoryService.getPhonesByStoreId(phoneInventoryEvent.getStoreId());

            log.info("Successfully processed INCREASE_QUANTITY for phoneId: {}, quantity: {}", phoneId, quantity);
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing INCREASE_QUANTITY payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for INCREASE_QUANTITY operation", e);
          } catch (Exception e) {
            log.error("Error processing INCREASE_QUANTITY: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process INCREASE_QUANTITY operation", e);
          }

        case "DECREASE_QUANTITY":
          log.debug("Processing DECREASE_QUANTITY with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> decreasePayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String phoneId = (String) decreasePayloadMap.get("phoneId");
            Integer quantity = (Integer) decreasePayloadMap.get("quantity");

            if (phoneId == null || quantity == null) {
              log.warn("Invalid DECREASE_QUANTITY data - phoneId: {}, quantity: {}", phoneId, quantity);
              return List.of();
            }

            log.debug("Decreasing quantity - phoneId: {}, quantity: {}", phoneId, quantity);

            // Decrease phone inventory
            phoneInventoryService.decreaseQuantityForSpecificPhoneId(phoneId, quantity);

            // Return updated inventory for the store
            result = phoneInventoryService.getPhonesByStoreId(phoneInventoryEvent.getStoreId());

            log.info("Successfully processed DECREASE_QUANTITY for phoneId: {}, quantity: {}", phoneId, quantity);
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing DECREASE_QUANTITY payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for DECREASE_QUANTITY operation", e);
          } catch (Exception e) {
            log.error("Error processing DECREASE_QUANTITY: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process DECREASE_QUANTITY operation", e);
          }

        default:
          log.warn("Unknown operation for UPDATE topic: {}, Event: {}",
              phoneInventoryEvent.getAction(), phoneInventoryEvent);
          return List.of();
      }
    } catch (Exception e) {
      log.error("Error handling UPDATE topic - Operation: {}, Event: {}, Error: {}",
          phoneInventoryEvent.getAction(), phoneInventoryEvent, e.getMessage(), e);
      return List.of();
    }
  }

  @KafkaListener(topics = KafkaTopics.PHONE_INVENTORY_DELETE, groupId = KafkaTopics.GROUP_ID)
  public List<PhoneInventoryItem> handleDeleteTopic(PhoneInventoryEvent phoneInventoryEvent) {
    log.info("Received DELETE topic message - Operation: {}, Event: {}",
        phoneInventoryEvent.getAction(), phoneInventoryEvent);

    try {
      List<PhoneInventoryItem> result;
      switch (phoneInventoryEvent.getAction()) {
        case "DELETE_PHONES_BY_IDS":
          log.debug("Processing DELETE_PHONES_BY_IDS with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> deletePayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            @SuppressWarnings("unchecked")
            List<String> phoneIds = (List<String>) deletePayloadMap.get("phoneIds");

            if (phoneIds == null || phoneIds.isEmpty()) {
              log.warn("No phone IDs found in DELETE_PHONES_BY_IDS payload");
              return List.of();
            }

            log.debug("Deleting {} phones by IDs", phoneIds.size());

            // Delete phones by IDs
            phoneInventoryService.deletePhonesByIds(phoneIds);

            // Return empty list as phones are deleted
            result = List.of();

            log.info("Successfully processed DELETE_PHONES_BY_IDS for {} phone IDs", phoneIds.size());
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing DELETE_PHONES_BY_IDS payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for DELETE_PHONES_BY_IDS operation", e);
          } catch (Exception e) {
            log.error("Error processing DELETE_PHONES_BY_IDS: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process DELETE_PHONES_BY_IDS operation", e);
          }

        case "DELETE_PHONE_BY_ID":
          log.debug("Processing DELETE_PHONE_BY_ID with payload: {}", phoneInventoryEvent.getPayload());

          try {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> singleDeletePayloadMap = (java.util.Map<String, Object>) phoneInventoryEvent.getPayload();

            String phoneId = (String) singleDeletePayloadMap.get("phoneId");

            if (phoneId == null || phoneId.trim().isEmpty()) {
              log.warn("Invalid phone ID in DELETE_PHONE_BY_ID payload: {}", phoneId);
              return List.of();
            }

            log.debug("Deleting phone by ID: {}", phoneId);

            // Delete phone by ID
            phoneInventoryService.deletePhoneById(phoneId);

            // Return empty list as phone is deleted
            result = List.of();

            log.info("Successfully processed DELETE_PHONE_BY_ID for phoneId: {}", phoneId);
            return result;

          } catch (ClassCastException e) {
            log.error("Error parsing DELETE_PHONE_BY_ID payload structure: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid payload structure for DELETE_PHONE_BY_ID operation", e);
          } catch (Exception e) {
            log.error("Error processing DELETE_PHONE_BY_ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process DELETE_PHONE_BY_ID operation", e);
          }
        case "DELETE_PHONE_BY_STORE_ID":
          log.debug("Processing DELETE_PHONE_BY_STORE_ID with storeId: {}", phoneInventoryEvent.getStoreId());

          try {
            String storeId = phoneInventoryEvent.getStoreId();

            if (storeId == null || storeId.trim().isEmpty()) {
              log.warn("Invalid store ID in DELETE_PHONE_BY_STORE_ID: {}", storeId);
              return List.of();
            }

            log.debug("Deleting all phones for store ID: {}", storeId);

            // Delete all phones by store ID
            phoneInventoryService.deletePhonesByStoreId(storeId);

            // Return empty list as phones are deleted
            result = List.of();

            log.info("Successfully processed DELETE_PHONE_BY_STORE_ID for storeId: {}", storeId);
            return result;

          } catch (Exception e) {
            log.error("Error processing DELETE_PHONE_BY_STORE_ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process DELETE_PHONE_BY_STORE_ID operation", e);
          }
        default:
          log.warn("Unknown operation for DELETE topic: {}, Event: {}",
              phoneInventoryEvent.getAction(), phoneInventoryEvent);
          return List.of();
      }
    } catch (Exception e) {
      log.error("Error handling DELETE topic - Operation: {}, Event: {}, Error: {}",
          phoneInventoryEvent.getAction(), phoneInventoryEvent, e.getMessage(), e);
      return List.of();
    }
  }
}