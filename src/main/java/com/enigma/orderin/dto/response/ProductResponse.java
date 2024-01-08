package com.enigma.orderin.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private Integer productId;
    private String productName;
    private Long price;
    private Integer stock;
    private Boolean isActive;
}
