package com.phonestore.service;


import com.phonestore.model.PhoneInventoryItemRequest;
import com.phonestore.repository.PhoneRepository;
import com.phonestore.model.PhoneInventoryItem;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhoneInventoryService {

  @Autowired
  private PhoneRepository repository;

  public PhoneInventoryItem getById(String id) {
    return repository.findById(id).orElse(null);
  }

  public List<PhoneInventoryItem> getByIds(List<String> ids) {
    return (List<PhoneInventoryItem>) repository.findAllById(ids);
  }

  public List<PhoneInventoryItem> getByModel(String model) {
    return repository.findByModel(model);
  }

  public List<PhoneInventoryItem> getByModels(List<String> models) {
    // Convert input models to uppercase for case-insensitive comparison
    List<String> upperCaseModels = models.stream()
        .map(String::toUpperCase)
        .collect(Collectors.toList());

    return repository.findAll().stream()
        .filter(item -> upperCaseModels.contains(item.getModel().toString().toUpperCase()))
        .collect(Collectors.toList());
  }

  public List<PhoneInventoryItem> addPhones(List<PhoneInventoryItemRequest> requests,String storeId) {
    List<PhoneInventoryItem> items = requests.stream().map(request -> {
      PhoneInventoryItem item = new PhoneInventoryItem();

      // Copy properties from request to item
      item.setModel(PhoneInventoryItem.ModelEnum.valueOf(request.getModel().name()));
      item.setPrice(request.getPrice());
      item.setQuantity(request.getQuantity());

      // Generate new UUID for each item
      item.setId(UUID.randomUUID());
      // Set store ID
      item.setStoreId(storeId);
      // Set timestamp
      item.setDateAdded(OffsetDateTime.now());
      if(item.getQuantity() > 0)
        item.setIsAvailable(true);
      else
        item.setIsAvailable(false);
      return item;

    }).collect(Collectors.toList());

    return repository.saveAll(items);
  }

  public void deletePhoneById(String id) {
    try {
      // Check if the phone exists before attempting to delete
      if (!repository.existsById(id)) {
        throw new IllegalArgumentException("Phone with ID " + id + " not found");
      }
      repository.deleteById(id);
    } catch (IllegalArgumentException e) {
      // Re-throw our custom exception or UUID format exception
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Error deleting phone with ID: " + id, e);
    }
  }

  public void deletePhonesByIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }

    // Validate that all IDs are valid UUIDs before proceeding
    for (String id : ids) {
      try {
        UUID.fromString(id);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid UUID format for phone ID: " + id, e);
      }
    }

    // Check which IDs don't exist
    List<String> existingIds = new ArrayList<>();
    List<String> nonExistingIds = new ArrayList<>();

    for (String id : ids) {
      if (repository.existsById(id)) {
        existingIds.add(id);
      } else {
        nonExistingIds.add(id);
      }
    }

    if (!nonExistingIds.isEmpty()) {
      throw new IllegalArgumentException("Phones with IDs not found: " + nonExistingIds);
    }

    // Delete using String IDs directly
    repository.deleteAllById(existingIds);
  }

  public void increaseQuantity(String id, int amount) {
    repository.findById(id).ifPresent(item -> {
      item.setQuantity(item.getQuantity() + amount);
      repository.save(item);
    });
  }

  public void decreaseQuantity(String id, int amount, String orderStoreId) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }

    repository.findByIdAndStoreId(UUID.fromString(id), orderStoreId).ifPresent(item -> {
      if (amount > item.getQuantity()) {
        throw new IllegalArgumentException("Cannot decrease quantity by " + amount +
            ". Current quantity is only " + item.getQuantity());
      }

      item.setQuantity(item.getQuantity() - amount);
      repository.save(item);
    });
  }

  public void decreaseQuantityForSpecificPhoneId(String id, int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }

    repository.findById(UUID.fromString(id).toString()).ifPresent(item -> {
      if (amount > item.getQuantity()) {
        throw new IllegalArgumentException("Cannot decrease quantity by " + amount +
            ". Current quantity is only " + item.getQuantity());
      }

      item.setQuantity(item.getQuantity() - amount);
      repository.save(item);
    });
  }

  public List<PhoneInventoryItem> getAll() {
    return repository.findAll();
  }
  public List<PhoneInventoryItem> getPhonesByStoreId(String storeId) {
    return repository.findByStoreId(storeId);
  }

  public void deletePhonesByStoreId(String storeId) {
    if (storeId == null || storeId.trim().isEmpty()) {
      throw new IllegalArgumentException("Store ID cannot be null or empty");
    }

    try {
      // Get all phones for the store to check if any exist
      List<PhoneInventoryItem> phonesToDelete = repository.findByStoreId(storeId);

      if (phonesToDelete.isEmpty()) {
        throw new IllegalArgumentException("No phones found for store ID: " + storeId);
      }

      // Delete all phones for the store
      repository.deleteByStoreId(storeId);

    } catch (IllegalArgumentException e) {
      // Re-throw our custom exception
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Error deleting phones for store ID: " + storeId, e);
    }
  }
}
