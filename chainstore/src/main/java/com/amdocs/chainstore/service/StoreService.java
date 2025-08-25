package com.amdocs.chainstore.service;

import com.amdocs.chainstore.exception.DuplicateStoreException;
import com.amdocs.chainstore.exception.StoreNotFoundException;
import com.amdocs.chainstore.kafka.PhoneInventoryEvent;
import com.amdocs.chainstore.kafka.PhoneStoreProducer;
import com.amdocs.chainstore.model.Store;
import com.amdocs.chainstore.model.StoreCreateRequest;
import com.amdocs.chainstore.model.StoreUpdateRequest;
import com.amdocs.chainstore.repository.StoreRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

  @Autowired
  private StoreRepository repository;

  @Autowired
  private PhoneStoreProducer phoneStoreProducer;

  public Store createStore(StoreCreateRequest request) {
    // Check if store with same name and address already exists
    if (repository.existsByStoreNameAndAddress(request.getStoreName(), request.getAddress())) {
      throw new DuplicateStoreException("Store with name '" + request.getStoreName() +
          "' and address '" + request.getAddress() + "' already exists");
    }

    Store store = new Store();
    store.setStoreName(request.getStoreName());
    store.setAddress(request.getAddress());
    store.setManagerName(request.getManagerName());
    return repository.save(store);
  }

  public Store getStore(String id) {
    return repository.findById(id)
        .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + id));
  }

  public void deleteStore(String id) {
    if (!repository.existsById(id)) {
      throw new StoreNotFoundException("Store not found with id: " + id);
    }

    // Publish store deletion event to Kafka
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("DELETE_PHONE_BY_STORE_ID");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());
    event.setStoreId(id);

    phoneStoreProducer.publishToDeleteTopic(event);

    repository.deleteById(id);
  }

  public Store updateStore(StoreUpdateRequest request) {
    Store store = repository.findById(request.getId())
        .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + request.getId()));

    // ID is not updated
    store.setStoreName(request.getStoreName());
    store.setAddress(request.getAddress());
    store.setManagerName(request.getManagerName());

    return repository.save(store);
  }

  // Phone Inventory Management Methods

  public Map<String, Object> orderPhones(Map<String, Object> orderRequest) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("ORDER_PHONES");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());
    event.setStoreId((String) orderRequest.get("storeId"));
    event.setPayload(orderRequest);

    phoneStoreProducer.publishToUpdateTopic(event);

    Map<String, Object> response = new HashMap<>();
    response.put("status", "Order request sent");
    response.put("requestId", event.getRequestId());
    response.put("message", "Phone order placed successfully - quantities will be decreased");

    return response;
  }

  public Map<String, Object> increasePhoneQuantity(Map<String, Object> quantityRequest) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("INCREASE_QUANTITY");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());
    event.setPayload(quantityRequest);

    phoneStoreProducer.publishToUpdateTopic(event);

    Map<String, Object> response = new HashMap<>();
    response.put("status", "Increase quantity request sent");
    response.put("requestId", event.getRequestId());
    return response;
  }

  public Map<String, Object> decreasePhoneQuantity(Map<String, Object> quantityRequest) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("DECREASE_QUANTITY");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());
    event.setPayload(quantityRequest);

    phoneStoreProducer.publishToUpdateTopic(event);

    Map<String, Object> response = new HashMap<>();
    response.put("status", "Decrease quantity request sent");
    response.put("requestId", event.getRequestId());
    return response;
  }
  public Map<String, Object> getPhoneById(String id) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_PHONE_BY_ID");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("phoneId", id);
    event.setPayload(payload);

    phoneStoreProducer.publishToGetTopic(event);

    // In a real implementation, you would wait for the response from Kafka
    // For now, returning a placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get phone request sent");
    response.put("requestId", event.getRequestId());
    response.put("phoneId", id);
    return response;
  }

  public List<Map<String, Object>> getPhonesByIds(List<String> ids) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_PHONES_BY_IDS");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("phoneIds", ids);
    event.setPayload(payload);

    phoneStoreProducer.publishToGetTopic(event);

    // Placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get phones request sent");
    response.put("requestId", event.getRequestId());
    response.put("phoneIds", ids);
    return List.of(response);
  }

  public List<Map<String, Object>> getPhonesByModel(String model) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_PHONES_BY_MODEL");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("model", model);
    event.setPayload(payload);

    phoneStoreProducer.publishToGetTopic(event);

    // Placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get phones by model request sent");
    response.put("requestId", event.getRequestId());
    response.put("model", model);
    return List.of(response);
  }

  public List<Map<String, Object>> getPhonesByModels(List<String> models) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_PHONES_BY_MODELS");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("models", models);
    event.setPayload(payload);

    phoneStoreProducer.publishToGetTopic(event);

    // Placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get phones by models request sent");
    response.put("requestId", event.getRequestId());
    response.put("models", models);
    return List.of(response);
  }

  public List<Map<String, Object>> getAllPhones() {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_ALL_PHONES");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    // No payload needed for get all phones
    event.setPayload(new HashMap<>());

    phoneStoreProducer.publishToGetTopic(event);

    // Placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get all phones request sent");
    response.put("requestId", event.getRequestId());
    return List.of(response);
  }

  public void deletePhoneById(String id) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("DELETE_PHONE_BY_ID");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("phoneId", id);
    event.setPayload(payload);

    phoneStoreProducer.publishToDeleteTopic(event);
  }

  public void deletePhonesByIds(List<String> ids) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("DELETE_PHONES_BY_IDS");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());

    Map<String, Object> payload = new HashMap<>();
    payload.put("phoneIds", ids);
    event.setPayload(payload);

    phoneStoreProducer.publishToDeleteTopic(event);
  }

  public Map<String, Object> addPhones(List<Map<String, Object>> phonesRequest, String storeId) {
    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("ADD_PHONES");
    event.setStoreId(storeId);
    event.setPayload(phonesRequest);

    phoneStoreProducer.publishToUpdateTopic(event);

    Map<String, Object> response = new HashMap<>();
    response.put("status", "Add phones request sent");
    response.put("phoneCount", phonesRequest.size());

    return response;
  }
  public Map<String, Object> getPhonesByStoreId(String storeId) {
    // Get store details
    Store store = getStore(storeId);
    if (store == null) {
      throw new RuntimeException("Store not found with storeId: " + storeId);
    }

    PhoneInventoryEvent event = new PhoneInventoryEvent();
    event.setAction("GET_PHONES_BY_STORE_ID");
    event.setRequestId(UUID.randomUUID().toString());
    event.setTimestamp(System.currentTimeMillis());
    event.setStoreId(storeId);

    Map<String, Object> payload = new HashMap<>();
    payload.put("storeId", storeId);
    event.setPayload(payload);

    phoneStoreProducer.publishToGetTopic(event);

    // Placeholder response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "Get phones by store ID request sent");
    response.put("requestId", event.getRequestId());
    response.put("storeId", storeId);
    response.put("storeName", store.getStoreName());

    return response;
  }

}

