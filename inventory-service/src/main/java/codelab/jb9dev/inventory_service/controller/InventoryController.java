package codelab.jb9dev.inventory_service.controller;

import codelab.jb9dev.inventory_service.dto.InventoryResponseDataDTO;
import codelab.jb9dev.inventory_service.dto.InventoryResponseErrorDTO;
import codelab.jb9dev.inventory_service.dto.InventoryResponseIsInStockDTO;
import codelab.jb9dev.inventory_service.exception.InventoryNotFoundException;
import codelab.jb9dev.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{sku-code}")
    public InventoryResponseDataDTO showInventory(@PathVariable(name = "sku-code") String skuCode) {
        return inventoryService.getInventory(skuCode);
    }

    @GetMapping("/is-in-stock")
    public List<InventoryResponseIsInStockDTO> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(InventoryNotFoundException.class)
    public InventoryResponseErrorDTO inventoryNotFound(InventoryNotFoundException exception) {
        return new InventoryResponseErrorDTO(exception.getMessage());
    }
}
