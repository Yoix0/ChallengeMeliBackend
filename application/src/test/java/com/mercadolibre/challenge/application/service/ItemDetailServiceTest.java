package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.ItemDetailResponse;
import com.mercadolibre.challenge.domain.common.exception.item_detail.ItemNotActiveException;
import com.mercadolibre.challenge.domain.common.exception.item_detail.ItemNotFoundException;
import com.mercadolibre.challenge.domain.item_detail.*;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemDetailServiceTest {
    
    @Mock
    private ItemRepository itemRepository;
    
    private ItemDetailService itemDetailService;
    
    @BeforeEach
    void setUp() {
        itemDetailService = new ItemDetailService(itemRepository);
    }
    
    @Test
    void getItemDetail_WhenItemExists_ShouldReturnItemDetailResponse() {
        // Arrange
        String itemId = "MLA123456789";
        Item item = createTestItem(itemId, "active");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        
        // Act
        ItemDetailResponse result = itemDetailService.getItemDetail(itemId);
        
        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("iPhone 15 Pro", result.getTitle());
        assertEquals("active", result.getStatus());
        verify(itemRepository).findById(itemId);
    }
    
    @Test
    void getItemDetail_WhenItemNotFound_ShouldThrowItemNotFoundException() {
        // Arrange
        String itemId = "MLA999999999";
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        
        // Act & Assert
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemDetailService.getItemDetail(itemId)
        );
        
        assertEquals("Item not found with ID: " + itemId, exception.getMessage());
        verify(itemRepository).findById(itemId);
    }
    
    @Test
    void getItemDetail_WhenItemNotActive_ShouldThrowItemNotActiveException() {
        // Arrange
        String itemId = "MLA123456789";
        Item item = createTestItem(itemId, "inactive");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        
        // Act & Assert
        ItemNotActiveException exception = assertThrows(
                ItemNotActiveException.class,
                () -> itemDetailService.getItemDetail(itemId)
        );
        
        assertEquals("Item is not active with ID: " + itemId, exception.getMessage());
        verify(itemRepository).findById(itemId);
    }
    
    @Test
    void getItemDetail_WhenItemIdIsNull_ShouldThrowException() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> itemDetailService.getItemDetail(null)
        );
        
        verifyNoInteractions(itemRepository);
    }
    
    @Test
    void getItemDetail_WhenItemIdIsEmpty_ShouldThrowException() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> itemDetailService.getItemDetail("")
        );
        
        verifyNoInteractions(itemRepository);
    }
    
    private Item createTestItem(String itemId, String status) {
        Price price = Price.of(1299999L, "ARS", 2);
        Category category = Category.from("MLA1055", "Celulares", "Electrónicos > Celulares");
        Seller seller = Seller.from(12345L, "TECHSTORE", null, null, 
                "AR", "5_green", "platinum", 100, 5, 
                BigDecimal.valueOf(0.98), BigDecimal.valueOf(0.01), BigDecimal.valueOf(0.01));
        
        return Item.from(
                itemId,
                "iPhone 15 Pro",
                price,
                "new",
                25,
                150,
                "https://articulo.mercadolibre.com.ar/" + itemId,
                status,
                "iPhone 15 Pro con chip A17 Pro",
                "gold_special",
                "buy_it_now",
                true,
                true,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now(),
                category,
                seller,
                List.of(),
                List.of(),
                List.of(),
                null,
                null
        );
    }
}