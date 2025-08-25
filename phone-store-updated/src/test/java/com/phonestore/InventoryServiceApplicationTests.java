package com.phonestore;

import com.phonestore.model.PhoneInventoryItem;
import com.phonestore.model.PhoneInventoryItem.ModelEnum;
import com.phonestore.model.PhoneInventoryItemRequest;
import com.phonestore.repository.PhoneRepository;
import com.phonestore.service.PhoneInventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class InventoryServiceApplicationTests {

	@Mock
	private PhoneRepository repository;

	@InjectMocks
	private PhoneInventoryService phoneInventoryService;

	@Test
	void contextLoads() {
	}

//	// ========== GET BY ID TESTS ==========
//	@Test
//	void getById_ShouldReturnPhoneInventoryItem_WhenRepositoryFindsItemWithValidId() {
//		// Arrange
//		String validId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem expectedItem = new PhoneInventoryItem();
//		expectedItem.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
//		expectedItem.setModel(ModelEnum.APPLE);
//		expectedItem.setQuantity(10);
//
//		when(repository.findById(validId)).thenReturn(Optional.of(expectedItem));
//
//		// Act
//		PhoneInventoryItem result = phoneInventoryService.getById(validId);
//
//		// Assert
//		assertThat(result).isNotNull();
//		assertThat(result).isEqualTo(expectedItem);
//		verify(repository).findById(validId);
//	}
//
//	@Test
//	void getById_ShouldThrowException_WhenItemNotFound() {
//		// Arrange
//		String invalidId = "non-existent-id";
//		when(repository.findById(invalidId)).thenReturn(Optional.empty());
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.getById(invalidId))
//				.isInstanceOf(RuntimeException.class);
//		verify(repository).findById(invalidId);
//	}
//
//	@Test
//	void getById_ShouldThrowException_WhenIdIsNull() {
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.getById(null))
//				.isInstanceOf(IllegalArgumentException.class);
//	}
//
//	// ========== GET ALL TESTS ==========
//	@Test
//	void getAll_ShouldReturnAllItems_WhenItemsExist() {
//		// Arrange
//		PhoneInventoryItem item1 = new PhoneInventoryItem();
//		item1.setId(UUID.randomUUID());
//		item1.setModel(ModelEnum.APPLE);
//		item1.setQuantity(10);
//
//		PhoneInventoryItem item2 = new PhoneInventoryItem();
//		item2.setId(UUID.randomUUID());
//		item2.setModel(ModelEnum.SAMSUNG);
//		item2.setQuantity(15);
//
//		List<PhoneInventoryItem> expectedItems = Arrays.asList(item1, item2);
//		when(repository.findAll()).thenReturn(expectedItems);
//
//		// Act
//		List<PhoneInventoryItem> result = phoneInventoryService.getAll();
//
//		// Assert
//		assertThat(result).isNotNull();
//		assertThat(result).hasSize(2);
//		assertThat(result).containsExactlyElementsOf(expectedItems);
//		verify(repository).findAll();
//	}
//
//	@Test
//	void getAll_ShouldReturnEmptyList_WhenNoItemsExist() {
//		// Arrange
//		when(repository.findAll()).thenReturn(Arrays.asList());
//
//		// Act
//		List<PhoneInventoryItem> result = phoneInventoryService.getAll();
//
//		// Assert
//		assertThat(result).isNotNull();
//		assertThat(result).isEmpty();
//		verify(repository).findAll();
//	}
//
//	// ========== CREATE TESTS ==========
//	@Test
//	void addPhones_ShouldCreateAndReturnItems_WhenValidItemsProvided() {
//		// Arrange
//		PhoneInventoryItemRequest inputRequest = new PhoneInventoryItemRequest();
//		inputRequest.setQuantity(20);
//
//		List<PhoneInventoryItemRequest> inputItems = Arrays.asList(inputRequest);
//
//		PhoneInventoryItem savedItem = new PhoneInventoryItem();
//		savedItem.setId(UUID.randomUUID());
//		savedItem.setModel(ModelEnum.GOOGLE);
//		savedItem.setQuantity(20);
//
//		// Mock the repository behavior for the addPhones method
//		when(phoneInventoryService.addPhones(inputItems)).thenReturn(Arrays.asList(savedItem));
//
//		// Act
//		List<PhoneInventoryItem> result = phoneInventoryService.addPhones(inputItems);
//
//		// Assert
//		assertThat(result).isNotNull();
//		assertThat(result).hasSize(1);
//		assertThat(result.get(0).getId()).isNotNull();
//		assertThat(result.get(0).getModel()).isEqualTo(ModelEnum.GOOGLE);
//		assertThat(result.get(0).getQuantity()).isEqualTo(20);
//	}
//
//	@Test
//	void addPhones_ShouldThrowException_WhenItemListIsNull() {
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.addPhones(null))
//				.isInstanceOf(IllegalArgumentException.class);
//		verify(repository, never()).save(any());
//	}
//
//
//
//	// ========== DELETE TESTS ==========
//	@Test
//	void delete_ShouldDeleteItem_WhenItemExists() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem existingItem = new PhoneInventoryItem();
//		existingItem.setId(UUID.fromString(itemId));
//		existingItem.setModel(ModelEnum.APPLE);
//		existingItem.setQuantity(10);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(existingItem));
//		doNothing().when(repository).deleteById(itemId);
//
//		// Act
//		phoneInventoryService.deleteById(itemId);
//
//		// Assert
//		verify(repository).findById(itemId);
//		verify(repository).deleteById(itemId);
//	}
//
//	@Test
//	void delete_ShouldThrowException_WhenItemNotFound() {
//		// Arrange
//		String invalidId = "non-existent-id";
//		when(repository.findById(invalidId)).thenReturn(Optional.empty());
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.deleteById(invalidId))
//				.isInstanceOf(RuntimeException.class);
//		verify(repository).findById(invalidId);
//		verify(repository, never()).deleteById(any());
//	}
//
//	// ========== DECREASE QUANTITY TESTS ==========
//	@Test
//	void decreaseQuantity_ShouldDecreaseQuantity_WhenItemExists() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.SAMSUNG);
//		item.setQuantity(10);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.decreaseQuantity(itemId, 3);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(7);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
//
//	@Test
//	void decreaseQuantity_ShouldNotGoBelowZero_WhenAmountExceedsQuantity() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.GOOGLE);
//		item.setQuantity(5);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.decreaseQuantity(itemId, 10);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(0);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
//
//	@Test
//	void decreaseQuantity_ShouldThrowException_WhenItemNotFound() {
//		// Arrange
//		String invalidId = "non-existent-id";
//		when(repository.findById(invalidId)).thenReturn(Optional.empty());
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.decreaseQuantity(invalidId, 5))
//				.isInstanceOf(RuntimeException.class);
//		verify(repository).findById(invalidId);
//		verify(repository, never()).save(any());
//	}
//
//	@Test
//	void decreaseQuantity_ShouldThrowException_WhenAmountIsNegative() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.decreaseQuantity(itemId, -5))
//				.isInstanceOf(IllegalArgumentException.class);
//		verify(repository, never()).findById(any());
//		verify(repository, never()).save(any());
//	}
//
//	// ========== INCREASE QUANTITY TESTS ==========
//	@Test
//	void increaseQuantity_ShouldIncreaseQuantity_WhenItemExists() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.XIAOMI);
//		item.setQuantity(10);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.increaseQuantity(itemId, 5);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(15);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
//
//	@Test
//	void increaseQuantity_ShouldThrowException_WhenItemNotFound() {
//		// Arrange
//		String invalidId = "non-existent-id";
//		when(repository.findById(invalidId)).thenReturn(Optional.empty());
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.increaseQuantity(invalidId, 5))
//				.isInstanceOf(RuntimeException.class);
//		verify(repository).findById(invalidId);
//		verify(repository, never()).save(any());
//	}
//
//	@Test
//	void increaseQuantity_ShouldThrowException_WhenAmountIsNegative() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//
//		// Act & Assert
//		assertThatThrownBy(() -> phoneInventoryService.increaseQuantity(itemId, -5))
//				.isInstanceOf(IllegalArgumentException.class);
//		verify(repository, never()).findById(any());
//		verify(repository, never()).save(any());
//	}
//
//	@Test
//	void increaseQuantity_ShouldHandleLargeQuantityIncrease_WhenItemExists() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.APPLE);
//		item.setQuantity(100);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.increaseQuantity(itemId, 1000);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(1100);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
//
//	// ========== EDGE CASE TESTS ==========
//	@Test
//	void decreaseQuantity_ShouldSetToZero_WhenQuantityIsExactlyEqualToDecrease() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.ONE_PLUS);
//		item.setQuantity(5);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.decreaseQuantity(itemId, 5);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(0);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
//
//	@Test
//	void increaseQuantity_ShouldWork_WhenQuantityIsZero() {
//		// Arrange
//		String itemId = "123e4567-e89b-12d3-a456-426614174000";
//		PhoneInventoryItem item = new PhoneInventoryItem();
//		item.setId(UUID.fromString(itemId));
//		item.setModel(ModelEnum.XIAOMI);
//		item.setQuantity(0);
//
//		when(repository.findById(itemId)).thenReturn(Optional.of(item));
//		when(repository.save(any(PhoneInventoryItem.class))).thenReturn(item);
//
//		// Act
//		phoneInventoryService.increaseQuantity(itemId, 10);
//
//		// Assert
//		assertThat(item.getQuantity()).isEqualTo(10);
//		verify(repository).findById(itemId);
//		verify(repository).save(item);
//	}
}