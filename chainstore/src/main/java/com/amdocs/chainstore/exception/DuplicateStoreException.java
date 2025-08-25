package com.amdocs.chainstore.exception;

public class DuplicateStoreException extends RuntimeException {
  public DuplicateStoreException(String message) {
    super(message);
  }

  public DuplicateStoreException(String message, Throwable cause) {
    super(message, cause);
  }
}