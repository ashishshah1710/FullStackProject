package com.amdocs.chainstore.controller;

import com.amdocs.chainstore.model.Store;
import com.amdocs.chainstore.model.StoreCreateRequest;
import com.amdocs.chainstore.model.StoreUpdateRequest;
import com.amdocs.chainstore.service.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

  @Autowired
  private StoreService storeService;


  @PostMapping("/createStore")
  public ResponseEntity<Store> createStore(@RequestBody @Valid StoreCreateRequest request) {
    return new ResponseEntity<>(storeService.createStore(request), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Store> getStore(@PathVariable String id) {
    return ResponseEntity.ok(storeService.getStore(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStore(@PathVariable String id) {
    storeService.deleteStore(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/updateStore")
  public ResponseEntity<Store> updateStore(@RequestBody @Valid StoreUpdateRequest request) {
    Store updatedStore = storeService.updateStore(request);
    return ResponseEntity.ok(updatedStore);

  }

  // Phone Inventory Management Endpoints

  @PostMapping("/orderPhones")
  public ResponseEntity<Map<String, Object>> orderPhones(@RequestBody @Valid Map<String, Object> orderRequest) {
    Map<String, Object> result = storeService.orderPhones(orderRequest);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/increaseQuantity")
  public ResponseEntity<Map<String, Object>> increaseQuantity(@RequestBody @Valid Map<String, Object> quantityRequest) {
    Map<String, Object> result = storeService.increasePhoneQuantity(quantityRequest);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/decreaseQuantity")
  public ResponseEntity<Map<String, Object>> decreaseQuantity(@RequestBody @Valid Map<String, Object> quantityRequest) {
    Map<String, Object> result = storeService.decreasePhoneQuantity(quantityRequest);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<Map<String, Object>> getPhoneById(@PathVariable String id) {
    Map<String, Object> phone = storeService.getPhoneById(id);
    return ResponseEntity.ok(phone);
  }

  @GetMapping("/ids")
  public ResponseEntity<List<Map<String, Object>>> getPhonesByIds(@RequestParam List<String> ids) {
    List<Map<String, Object>> phones = storeService.getPhonesByIds(ids);
    return ResponseEntity.ok(phones);
  }

  @GetMapping("/model/{model}")
  public ResponseEntity<List<Map<String, Object>>> getPhonesByModel(@PathVariable String model) {
    List<Map<String, Object>> phones = storeService.getPhonesByModel(model);
    return ResponseEntity.ok(phones);
  }

  @GetMapping("/models")
  public ResponseEntity<List<Map<String, Object>>> getPhonesByModels(@RequestParam List<String> models) {
    List<Map<String, Object>> phones = storeService.getPhonesByModels(models);
    return ResponseEntity.ok(phones);
  }

  @GetMapping("/catalog")
  public ResponseEntity<List<Map<String, Object>>> getAllPhones() {
    List<Map<String, Object>> catalog = storeService.getAllPhones();
    return ResponseEntity.ok(catalog);
  }

  @DeleteMapping("/id/{id}")
  public ResponseEntity<Void> deletePhoneById(@PathVariable String id) {
    storeService.deletePhoneById(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/ids")
  public ResponseEntity<Void> deletePhonesByIds(@RequestParam @NotEmpty(message = "IDs list cannot be empty") List<String> ids) {
    storeService.deletePhonesByIds(ids);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{storeId}/addPhones")
  public ResponseEntity<Map<String, Object>> addPhones(@PathVariable("storeId") String storeId,@RequestBody @Valid List<Map<String, Object>> phonesRequest) {
    if(storeService.getStore(storeId) != null) {
      Map<String, Object> result = storeService.addPhones(phonesRequest, storeId);
      return new ResponseEntity<>(result, HttpStatus.CREATED);
    }else {
      throw new RuntimeException("Store not found with storeId: " + storeId);
    }
  }
  @GetMapping("/{storeId}/phones")
  public ResponseEntity<Map<String, Object>> fetchPhonesByStoreId(@PathVariable String storeId) {
    if(storeService.getStore(storeId) != null) {
      Map<String, Object> result = storeService.getPhonesByStoreId(storeId);
      return ResponseEntity.ok(result);
    } else {
      throw new RuntimeException("Store not found with storeId: " + storeId);
    }
  }
}

