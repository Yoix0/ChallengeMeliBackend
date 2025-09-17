package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.ItemDetailResponse;
import com.mercadolibre.challenge.application.port.in.GetItemDetailUseCase;
import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;
import com.mercadolibre.challenge.domain.common.exception.item_detail.ItemNotActiveException;
import com.mercadolibre.challenge.domain.common.exception.item_detail.ItemNotFoundException;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemDetailService implements GetItemDetailUseCase {
    
    private final ItemRepository itemRepository;
    
    public ItemDetailService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    @Override
    public ItemDetailResponse getItemDetail(String itemId) {
        ValidateArgument.validateStringNotNullAndNotEmpty(itemId, "itemId");
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        
        if (!item.isActive()) {
            throw new ItemNotActiveException(itemId);
        }
        
        return mapToResponse(item);
    }
    
    private ItemDetailResponse mapToResponse(Item item) {
        ItemDetailResponse response = new ItemDetailResponse();
        
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setConditionType(item.getConditionType());
        response.setAvailableQuantity(item.getAvailableQuantity());
        response.setSoldQuantity(item.getSoldQuantity());
        response.setPermalink(item.getPermalink());
        response.setStatus(item.getStatus());
        response.setDescription(item.getDescription());
        response.setListingType(item.getListingType());
        response.setBuyingMode(item.getBuyingMode());
        response.setFreeShipping(item.getFreeShipping());
        response.setLocalPickUp(item.getLocalPickUp());
        response.setCreatedDate(item.getCreatedDate());
        response.setUpdatedDate(item.getUpdatedDate());
        
        if (item.getPrice() != null) {
            response.setPrice(new ItemDetailResponse.PriceDto(
                    item.getPrice().getAmount(),
                    item.getPrice().getCurrency(),
                    item.getPrice().getDecimals()
            ));
        }
        
        if (item.getCategory() != null) {
            response.setCategory(new ItemDetailResponse.CategoryDto(
                    item.getCategory().getId(),
                    item.getCategory().getName(),
                    item.getCategory().getPathFromRoot()
            ));
        }
        
        if (item.getSeller() != null) {
            ItemDetailResponse.SellerDto sellerDto = new ItemDetailResponse.SellerDto();
            sellerDto.setId(item.getSeller().getId());
            sellerDto.setNickname(item.getSeller().getNickname());
            sellerDto.setPermalink(item.getSeller().getPermalink());
            sellerDto.setRegistrationDate(item.getSeller().getRegistrationDate());
            sellerDto.setCountryId(item.getSeller().getCountryId());
            sellerDto.setReputationLevel(item.getSeller().getReputationLevel());
            sellerDto.setPowerSellerStatus(item.getSeller().getPowerSellerStatus());
            sellerDto.setTransactionsCompleted(item.getSeller().getTransactionsCompleted());
            sellerDto.setTransactionsCanceled(item.getSeller().getTransactionsCanceled());
            sellerDto.setRatingPositive(item.getSeller().getRatingPositive());
            sellerDto.setRatingNegative(item.getSeller().getRatingNegative());
            sellerDto.setRatingNeutral(item.getSeller().getRatingNeutral());
            response.setSeller(sellerDto);
        }
        
        if (item.getAttributes() != null) {
            response.setAttributes(
                    item.getAttributes().stream()
                            .map(attr -> new ItemDetailResponse.AttributeDto(
                                    attr.getAttributeId(),
                                    attr.getName(),
                                    attr.getValue(),
                                    attr.getUnit(),
                                    attr.getValueType().getCode()
                            ))
                            .toList()
            );
        }
        
        if (item.getPictures() != null) {
            response.setPictures(
                    item.getPictures().stream()
                            .map(pic -> {
                                ItemDetailResponse.PictureDto picDto = new ItemDetailResponse.PictureDto();
                                picDto.setPictureId(pic.getPictureId());
                                picDto.setUrl(pic.getUrl());
                                picDto.setSecureUrl(pic.getSecureUrl());
                                picDto.setSize(pic.getSize());
                                picDto.setMaxSize(pic.getMaxSize());
                                picDto.setQuality(pic.getQuality());
                                picDto.setOrder(pic.getOrder());
                                return picDto;
                            })
                            .toList()
            );
        }
        
        if (item.getShippingMethods() != null) {
            response.setShippingMethods(
                    item.getShippingMethods().stream()
                            .map(shipping -> {
                                ItemDetailResponse.ShippingMethodDto shippingDto = new ItemDetailResponse.ShippingMethodDto();
                                shippingDto.setMethodId(shipping.getMethodId());
                                shippingDto.setName(shipping.getName());
                                shippingDto.setType(shipping.getType());
                                shippingDto.setFreeShipping(shipping.getFreeShipping());
                                shippingDto.setEstimatedMinDays(shipping.getEstimatedMinDays());
                                shippingDto.setEstimatedMaxDays(shipping.getEstimatedMaxDays());
                                shippingDto.setLocalPickUp(shipping.getLocalPickUp());
                                
                                if (shipping.getCost() != null) {
                                    shippingDto.setCost(new ItemDetailResponse.PriceDto(
                                            shipping.getCost().getAmount(),
                                            shipping.getCost().getCurrency(),
                                            shipping.getCost().getDecimals()
                                    ));
                                }
                                return shippingDto;
                            })
                            .toList()
            );
        }
        
        if (item.getPaymentMethod() != null) {
            ItemDetailResponse.PaymentMethodDto paymentDto = new ItemDetailResponse.PaymentMethodDto();
            paymentDto.setInstallmentsQuantity(item.getPaymentMethod().getInstallmentsQuantity());
            paymentDto.setInstallmentsRate(item.getPaymentMethod().getInstallmentsRate());
            paymentDto.setInstallmentAmount(item.getPaymentMethod().getInstallmentAmount());
            paymentDto.setAcceptsCreditCard(item.getPaymentMethod().getAcceptsCreditCard());
            paymentDto.setAcceptsDebitCard(item.getPaymentMethod().getAcceptsDebitCard());
            paymentDto.setAcceptsMercadoPago(item.getPaymentMethod().getAcceptsMercadoPago());
            response.setPaymentMethod(paymentDto);
        }
        
        if (item.getWarranty() != null) {
            response.setWarranty(new ItemDetailResponse.WarrantyDto(
                    item.getWarranty().getType(),
                    item.getWarranty().getTime(),
                    item.getWarranty().getDescription()
            ));
        }
        
        return response;
    }
}