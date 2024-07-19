package codelab.jb9dev.inventory_service.model;

import codelab.jb9dev.inventory_service.dto.InventoryResponseDataDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_inventory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;

    public Inventory (InventoryResponseDataDTO dto) {
        this.skuCode = dto.getSkuCode();
        this.quantity = dto.getQuantity();
    }
}
