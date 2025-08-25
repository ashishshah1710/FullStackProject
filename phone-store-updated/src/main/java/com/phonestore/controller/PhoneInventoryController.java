package com.phonestore.controller;

import com.phonestore.api.InventoryApi;
import com.phonestore.model.PhoneInventoryItem;
import com.phonestore.model.PhoneInventoryItemRequest;
import com.phonestore.service.PhoneInventoryService;
import com.phonestore.exception.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@Validated
public class PhoneInventoryController implements InventoryApi {

  private final PhoneInventoryService service;

  @Autowired
  public PhoneInventoryController(PhoneInventoryService service) {
    this.service = service;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<PhoneInventoryItem> getById(@PathVariable String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new ValidationException("Phone ID cannot be null or empty");
    }
    PhoneInventoryItem item = service.getById(id);
    return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
  }

  @GetMapping("/ids")
  public ResponseEntity<List<PhoneInventoryItem>> getByIds(
      @RequestParam @NotEmpty(message = "IDs list cannot be empty") List<String> ids) {
    if (ids.stream().anyMatch(id -> id == null || id.trim().isEmpty())) {
      throw new ValidationException("All IDs must be valid and non-empty");
    }
    return ResponseEntity.ok(service.getByIds(ids));
  }

  @GetMapping("/model/{model}")
  public ResponseEntity<List<PhoneInventoryItem>> getByModel(@PathVariable String model) {
    if (model == null || model.trim().isEmpty()) {
      throw new ValidationException("Phone model cannot be null or empty");
    }
    return ResponseEntity.ok(service.getByModel(model));
  }

  @GetMapping("/models")
  public ResponseEntity<List<PhoneInventoryItem>> getByModels(
      @RequestParam @NotEmpty(message = "Models list cannot be empty") List<String> models) {
    if (models.stream().anyMatch(model -> model == null || model.trim().isEmpty())) {
      throw new ValidationException("All models must be valid and non-empty");
    }
    List<PhoneInventoryItem> result = new ArrayList<>();
    for (String model : models) {
      result.addAll(service.getByModel(model));
    }
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<List<PhoneInventoryItem>> addPhones(
      @RequestBody
      List<@Valid PhoneInventoryItemRequest> phoneRequests) {

    if (phoneRequests.stream().anyMatch(request ->
        request.getModel() == null || request.getModel().toString().trim().isEmpty())) {
      throw new ValidationException("All phone items must have a valid model");
    }

    List<PhoneInventoryItem> createdItems = service.addPhones(phoneRequests,null);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
  }
  @DeleteMapping("/id/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new ValidationException("Phone ID cannot be null or empty");
    }

    // Check if the item exists before attempting to delete
    PhoneInventoryItem item = service.getById(id);
    if (item == null) {
      return ResponseEntity.notFound().build();
    }

    service.deletePhoneById(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/ids")
  public ResponseEntity<Void> deleteByIds(
      @RequestParam @NotEmpty(message = "IDs list cannot be empty") List<String> ids) {
    if (ids.stream().anyMatch(id -> id == null || id.trim().isEmpty())) {
      throw new ValidationException("All IDs must be valid and non-empty");
    }
    service.deletePhonesByIds(ids);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/increase")
  public ResponseEntity<Void> increaseQuantity(
      @PathVariable String id,
      @RequestHeader("X-Increase-Amount") int amount) {
    if (id == null || id.trim().isEmpty()) {
      throw new ValidationException("Phone ID cannot be null or empty");
    }
    if (amount <= 0) {
      throw new ValidationException("Increase amount must be greater than 0");
    }
    service.increaseQuantity(id, amount);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/decrease")
  public ResponseEntity<Void> decreaseQuantity(@RequestBody Map<String, Object> body) {
    String id = (String) body.get("id");
    if (id == null || id.trim().isEmpty()) {
      throw new ValidationException("Phone ID cannot be null or empty");
    }

    Object amountObj = body.get("value");
    if (amountObj == null) {
      throw new ValidationException("Decrease amount is required");
    }

    int amount;
    try {
      amount = (Integer) amountObj;
    } catch (ClassCastException e) {
      throw new ValidationException("Decrease amount must be a valid integer");
    }

    if (amount <= 0) {
      throw new ValidationException("Decrease amount must be greater than 0");
    }

    service.decreaseQuantity(id, amount,null);
    return ResponseEntity.ok().build();
  }
}