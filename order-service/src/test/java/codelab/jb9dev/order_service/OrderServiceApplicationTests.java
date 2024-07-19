package codelab.jb9dev.order_service;

import codelab.jb9dev.order_service.dto.OrderLineItemsResponseDTO;
import codelab.jb9dev.order_service.dto.OrderRequestDTO;
import codelab.jb9dev.order_service.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
    static MySQLContainer<?> database = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void setPropertiesSource(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.driver-class-name", database::getDriverClassName);
    }

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        database.start();
    }

    @AfterAll
    static void afterAll() {
        database.stop();
    }

    @BeforeEach
    void clearDatabase() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldPlaceOrder() throws Exception {
        String orderRequestBody = getPlaceOrderRequestBody();
        // TODO: Add mock to Inventory Service, otherwise the mocked post request below will fail
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
        ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(1, orderRepository.findAll().size());
        Assertions.assertEquals("Order placed successfully", response);
    }

    private String getPlaceOrderRequestBody() throws JsonProcessingException {
        OrderRequestDTO orderRequestBody = new OrderRequestDTO();
        orderRequestBody.setOrderLineItems(getOrderLineItemsBody());

        return objectMapper.writeValueAsString(orderRequestBody);
    }

    private List<OrderLineItemsResponseDTO> getOrderLineItemsBody() {
        List<OrderLineItemsResponseDTO> orderLineItemsBody = new ArrayList<>();
        OrderLineItemsResponseDTO orderLineItem = OrderLineItemsResponseDTO.builder().skuCode("abc123").quantity(1)
                .price(BigDecimal.valueOf(1.00)).build();
        orderLineItemsBody.add(orderLineItem);

        return orderLineItemsBody;
    }
}
