package com.phonestore.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInventoryEvent {
  private String action;
  private String storeId;
  private Object payload;
  private String timestamp;
}

