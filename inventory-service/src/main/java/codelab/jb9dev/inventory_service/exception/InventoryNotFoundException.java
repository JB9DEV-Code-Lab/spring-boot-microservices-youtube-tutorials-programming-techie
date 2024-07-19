package codelab.jb9dev.inventory_service.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String skuCode) {
        super("Inventory with sku code " + skuCode + " not found");
    }
}
