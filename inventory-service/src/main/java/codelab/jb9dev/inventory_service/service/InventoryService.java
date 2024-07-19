package codelab.jb9dev.inventory_service.service;

import codelab.jb9dev.inventory_service.dto.InventoryResponseDataDTO;
import codelab.jb9dev.inventory_service.dto.InventoryResponseIsInStockDTO;
import codelab.jb9dev.inventory_service.exception.InventoryNotFoundException;
import codelab.jb9dev.inventory_service.model.Inventory;
import codelab.jb9dev.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryResponseDataDTO getInventory(String skuCode) throws InventoryNotFoundException {
        Optional<Inventory> foundInventory = inventoryRepository.findBySkuCode(skuCode);

        if (foundInventory.isEmpty()) {
            throw new InventoryNotFoundException(skuCode);
        }

        InventoryResponseDataDTO inventoryResponseDTO = new InventoryResponseDataDTO();
        inventoryResponseDTO.setSkuCode(foundInventory.get().getSkuCode());
        inventoryResponseDTO.setQuantity(foundInventory.get().getQuantity());

        return inventoryResponseDTO;
    }

    public List<InventoryResponseIsInStockDTO> isInStock(List<String> skuCodes) {
        List<String> uniqueSkuCodes = Set.copyOf(skuCodes).stream().toList();
        List<Inventory> foundInventories = inventoryRepository.findBySkuCodeIn(uniqueSkuCodes);
        List<InventoryResponseIsInStockDTO> inventoryResponseIsInStockDTOList = new ArrayList<>();


        if (foundInventories.size() < uniqueSkuCodes.size()) {
            List<String> foundInventoriesSkuCodes = foundInventories.stream()
                    .map(Inventory::getSkuCode).toList();

            uniqueSkuCodes.forEach(sku -> {
                if (!foundInventoriesSkuCodes.contains(sku)) {
                    inventoryResponseIsInStockDTOList.add(new InventoryResponseIsInStockDTO(sku, false));
                }
            });

            return inventoryResponseIsInStockDTOList;
        }

        return foundInventories.stream().map(inventory -> {
            InventoryResponseIsInStockDTO isInStockResponse = new InventoryResponseIsInStockDTO();
            isInStockResponse.setSkuCode(inventory.getSkuCode());
            isInStockResponse.setIsInStock(inventory.getQuantity() > 0);

            return isInStockResponse;
        }).toList();
    }
}
