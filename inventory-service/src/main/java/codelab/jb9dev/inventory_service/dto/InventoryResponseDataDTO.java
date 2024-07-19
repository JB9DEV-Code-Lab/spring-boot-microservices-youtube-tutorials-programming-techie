package codelab.jb9dev.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDataDTO {
    private String skuCode;
    private Integer quantity;
}
