package codelab.jb9dev.inventory_service;

import codelab.jb9dev.inventory_service.dto.InventoryResponseDataDTO;
import codelab.jb9dev.inventory_service.dto.InventoryResponseErrorDTO;
import codelab.jb9dev.inventory_service.dto.InventoryResponseIsInStockDTO;
import codelab.jb9dev.inventory_service.model.Inventory;
import codelab.jb9dev.inventory_service.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.datasource.driver-class-name", database::getDriverClassName);
    }

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        database.start();
    }

    @AfterAll
    public static void afterAll() {
        database.stop();
    }

    @AfterEach
    public void afterEach() {
        inventoryRepository.deleteAll();
    }

    @Test
    void shouldShowInventoryData() throws Exception {
        InventoryResponseDataDTO expectedResponseDTO = addInventory("abc123", 1);
        MockHttpServletResponse response = getInventoryResponse("/api/inventory/abc123");

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(
                objectMapper.writeValueAsString(expectedResponseDTO),
                response.getContentAsString()
        );
    }

    @Test
    void shouldNotFindInventory() throws Exception {
        InventoryResponseErrorDTO expectedResponse = new InventoryResponseErrorDTO(
                "Inventory with sku code abc123 not found");
        MockHttpServletResponse response = getInventoryResponse("/api/inventory/abc123");

        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), response.getContentAsString());
    }

    // region Is In Stock requests
    @Test
    void shouldBeInInventory() throws Exception {
        addInventory("abc123", 1);
        List<InventoryResponseIsInStockDTO> expectedResponse = List.of(
                new InventoryResponseIsInStockDTO("abc123", true));

        MockHttpServletResponse response = getInventoryResponse("/api/inventory/is-in-stock?skuCode=abc123");
        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), response.getContentAsString());
    }

    @Test
    void shouldNotBeInInventory() throws Exception {
        addInventory("abc123", 0);
        List<InventoryResponseIsInStockDTO> expectedResponse = List.of(
                new InventoryResponseIsInStockDTO("abc123", false));

        MockHttpServletResponse response = getInventoryResponse("/api/inventory/is-in-stock?skuCode=abc123");
        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), response.getContentAsString());
    }

    @Test
    void shouldNotFindInventoryInStock() throws Exception {
        List<InventoryResponseIsInStockDTO> expectedResponse = List.of(
                new InventoryResponseIsInStockDTO("abc123", false));
        MockHttpServletResponse response = getInventoryResponse("/api/inventory/is-in-stock?skuCode=abc123");

        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), response.getContentAsString());
    }
    // endregion Is In Stock

    private InventoryResponseDataDTO addInventory(String skuCode, Integer quantity) {
        InventoryResponseDataDTO inventoryDataDTO = new InventoryResponseDataDTO(skuCode, quantity);
        Inventory inventory = new Inventory(inventoryDataDTO);
        inventoryRepository.save(inventory);

        return inventoryDataDTO;
    }

    private MockHttpServletResponse getInventoryResponse(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }
}
