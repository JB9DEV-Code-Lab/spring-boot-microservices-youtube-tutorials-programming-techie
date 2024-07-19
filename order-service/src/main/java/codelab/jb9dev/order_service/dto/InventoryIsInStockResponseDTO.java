package codelab.jb9dev.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryIsInStockResponseDTO {
    private String skuCode;
    private Boolean isInStock;
}
