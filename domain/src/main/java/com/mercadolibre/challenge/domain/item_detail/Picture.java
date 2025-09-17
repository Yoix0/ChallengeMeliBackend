package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.util.Objects;

public class Picture {
    
    private final String pictureId;
    private final String url;
    private final String secureUrl;
    private final String size;
    private final String maxSize;
    private final String quality;
    private final Integer order;
    
    private Picture(String pictureId, String url, String secureUrl, String size, 
                   String maxSize, String quality, Integer order) {
        this.pictureId = pictureId;
        this.url = url;
        this.secureUrl = secureUrl;
        this.size = size;
        this.maxSize = maxSize;
        this.quality = quality;
        this.order = order;
    }
    
    public static Picture of(String pictureId, String url, String secureUrl, String size,
                           String maxSize, String quality, Integer order) {
        validatePictureParameters(pictureId, url, secureUrl, order);
        return new Picture(
            pictureId.trim(),
            url.trim(),
            secureUrl.trim(),
            size != null ? size.trim() : "500x500",
            maxSize != null ? maxSize.trim() : "1200x1200",
            quality != null ? quality.trim() : "high",
            order != null ? order : 1
        );
    }
    
    private static void validatePictureParameters(String pictureId, String url, String secureUrl, Integer order) {
        ValidateArgument.validateStringNotNullAndNotEmpty(pictureId, "pictureId");
        ValidateArgument.validateStringNotNullAndNotEmpty(url, "url");
        ValidateArgument.validateStringNotNullAndNotEmpty(secureUrl, "secureUrl");
        
        ValidateArgument.validateLength(pictureId.length(), 1, 100, "pictureId");
        ValidateArgument.validateLength(url.length(), 1, 1000, "url");
        ValidateArgument.validateLength(secureUrl.length(), 1, 1000, "secureUrl");
        
        if (order != null && order < 1) {
            throw new IllegalArgumentException("El orden de la imagen debe ser mayor a 0");
        }
    }
    
    public String getPictureId() {
        return pictureId;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getSecureUrl() {
        return secureUrl;
    }
    
    public String getSize() {
        return size;
    }
    
    public String getMaxSize() {
        return maxSize;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(pictureId, picture.pictureId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pictureId);
    }
    
    @Override
    public String toString() {
        return String.format("Picture{id='%s', order=%d, size='%s'}", 
                pictureId, order, size);
    }
}