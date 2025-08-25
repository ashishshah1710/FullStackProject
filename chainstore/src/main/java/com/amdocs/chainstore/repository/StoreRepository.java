package com.amdocs.chainstore.repository;

import com.amdocs.chainstore.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store,String> {
  boolean existsByStoreNameAndAddress(String storeName, String address);


}
