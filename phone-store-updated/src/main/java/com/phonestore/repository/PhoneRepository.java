package com.phonestore.repository;

import java.util.List;
import com.phonestore.model.PhoneInventoryItem;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends MongoRepository<PhoneInventoryItem, String> {

  List<PhoneInventoryItem> findByModel(String model);
  Optional<PhoneInventoryItem> findByIdAndStoreId(UUID id, String storeId);
  List<PhoneInventoryItem> findByStoreId(String storeId);
  void deleteByStoreId(String storeId);


}
