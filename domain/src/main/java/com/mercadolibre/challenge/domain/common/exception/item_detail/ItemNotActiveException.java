package com.mercadolibre.challenge.domain.common.exception.item_detail;

import com.mercadolibre.challenge.domain.common.exception.BusinessException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

public class ItemNotActiveException extends BusinessException {
    
    public ItemNotActiveException(String itemId) {
        super(ExceptionCode.ITEM_NOT_ACTIVE, "Item is not active with ID: " + itemId);
    }
    
}