package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.ItemDetailResponse;

public interface GetItemDetailUseCase {
    
    ItemDetailResponse getItemDetail(String itemId);
    
}