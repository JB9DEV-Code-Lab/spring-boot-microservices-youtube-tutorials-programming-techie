package codelab.jb9dev.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsRequestDTO {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
