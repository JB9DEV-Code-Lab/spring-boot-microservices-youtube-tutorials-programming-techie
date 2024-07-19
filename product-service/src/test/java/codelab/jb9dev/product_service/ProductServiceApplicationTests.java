package codelab.jb9dev.product_service;

import codelab.jb9dev.product_service.dto.ProductRequest;
import codelab.jb9dev.product_service.dto.ProductResponse;
import codelab.jb9dev.product_service.model.Product;
import codelab.jb9dev.product_service.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.11");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void clearMongoDB() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        String productRequest = getRequestBody();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest)
        ).andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private String getRequestBody() throws JsonProcessingException {
        ProductRequest productRequest = ProductRequest.builder()
                .name("A nice product")
                .description("A nice and cheat product!")
                .price(BigDecimal.valueOf(10))
                .build();

        return objectMapper.writeValueAsString(productRequest);
    }

    @Test
    void shouldListEmptyProductList() {
        Assertions.assertEquals(0, productRepository.findAll().size());
    }

    @Test
    void shouldListCreatedProducts() throws Exception {
        Product product = Product.builder()
                .name("A nice product")
                .description("A nice and cheap product!")
                .price(BigDecimal.valueOf(10))
                .build();

        productRepository.save(product);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

		List<ProductResponse> productResponse = Arrays.asList(objectMapper.readValue(response, ProductResponse[].class));

		ProductResponse firstProduct = productResponse.getFirst();
		Assertions.assertEquals("A nice product", firstProduct.getName());
		Assertions.assertEquals("A nice and cheap product!", firstProduct.getDescription());
		Assertions.assertEquals(BigDecimal.valueOf(10), firstProduct.getPrice());
		Assertions.assertEquals(1, productResponse.size());
    }
}
