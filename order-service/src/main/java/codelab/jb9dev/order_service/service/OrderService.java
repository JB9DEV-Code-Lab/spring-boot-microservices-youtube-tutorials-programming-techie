package codelab.jb9dev.order_service.service;

import codelab.jb9dev.order_service.dto.InventoryIsInStockResponseDTO;
import codelab.jb9dev.order_service.dto.OrderLineItemsResponseDTO;
import codelab.jb9dev.order_service.dto.OrderRequestDTO;
import codelab.jb9dev.order_service.exception.InventoryIsOutOfStockException;
import codelab.jb9dev.order_service.model.Order;
import codelab.jb9dev.order_service.model.OrderLineItems;
import codelab.jb9dev.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Value("${inventory-service.base-url}")
    private String inventoryServiceBaseUrl;
    public void placeOrder(OrderRequestDTO orderRequestDTO) {
        InventoryIsInStockResponseDTO[] skuInventories = getSkusInventories(orderRequestDTO);
        if (!Arrays.stream(skuInventories).allMatch(InventoryIsInStockResponseDTO::getIsInStock)) {
        // TODO:
        //  - It should be interesting raising this exception also when the requested quantity is less than the
        //  - amount in the inventory. For this, it may be necessary changing the /api/inventory to accept request
        //  - parameters as well, and make this decision here, by comparing each available quantity with the order one
            throw new InventoryIsOutOfStockException(skuInventories);
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItems)
                .toList();

        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
    }

    private InventoryIsInStockResponseDTO[] getSkusInventories(OrderRequestDTO orderRequestDTO) {
        List<String> skuCodes = orderRequestDTO.getOrderLineItems().stream()
                .map(OrderLineItemsResponseDTO::getSkuCode).toList();

        InventoryIsInStockResponseDTO[] inventoryResponseArray = webClient.get()
                .uri(inventoryServiceBaseUrl + "/is-in-stock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryIsInStockResponseDTO[].class)
                .block();

        if (inventoryResponseArray == null) inventoryResponseArray = new InventoryIsInStockResponseDTO[]{};

        return inventoryResponseArray;
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsResponseDTO orderLineItemsRequestDTO) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsRequestDTO.getSkuCode())
                .price(orderLineItemsRequestDTO.getPrice())
                .quantity(orderLineItemsRequestDTO.getQuantity())
                .build();
    }
}
