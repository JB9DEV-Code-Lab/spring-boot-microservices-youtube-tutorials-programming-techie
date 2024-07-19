package codelab.jb9dev.order_service.controller;

import codelab.jb9dev.order_service.dto.ErrorResponseDTO;
import codelab.jb9dev.order_service.dto.OrderRequestDTO;
import codelab.jb9dev.order_service.exception.InventoryIsOutOfStockException;
import codelab.jb9dev.order_service.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDTO orderRequest) throws JsonProcessingException {
        orderService.placeOrder(orderRequest);

        return "Order placed successfully";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(InventoryIsOutOfStockException.class)
    public ErrorResponseDTO inventoryOutOfStock(InventoryIsOutOfStockException exception) {
        return new ErrorResponseDTO(exception.getMessage(), exception.getDetails());
    }
}
