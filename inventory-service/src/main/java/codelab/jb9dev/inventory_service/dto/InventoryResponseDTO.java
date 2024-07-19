package codelab.jb9dev.inventory_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {
    private InventoryResponseDataDTO data;
    private InventoryResponseErrorDTO error;

    public void setErrorMessage(String message) {
        error.setMessage(message);
    }
}
