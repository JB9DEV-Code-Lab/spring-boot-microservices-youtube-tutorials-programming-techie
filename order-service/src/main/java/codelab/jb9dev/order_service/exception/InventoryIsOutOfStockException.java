package codelab.jb9dev.order_service.exception;


import codelab.jb9dev.order_service.dto.InventoryIsInStockResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
public class InventoryIsOutOfStockException extends RuntimeException {
    private final List<String> details;

    public InventoryIsOutOfStockException(InventoryIsInStockResponseDTO[] inventoryIsInStockResponseDTOArray) {
        super("These products sku codes in the details list are out of stock:");
        this.details = Arrays.stream(inventoryIsInStockResponseDTOArray)
                .map(InventoryIsInStockResponseDTO::getSkuCode)
                .toList();
    }
}
