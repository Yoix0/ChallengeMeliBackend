package com.mercadolibre.challenge.domain.common.exception.item_detail;
import com.mercadolibre.challenge.domain.common.exception.BaseException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

public class ItemNotFoundException extends BaseException {
    
    public ItemNotFoundException(String itemId) {
        super(ExceptionCode.ITEM_NOT_FOUND, "Item not found with ID: " + itemId);
    }
    
}