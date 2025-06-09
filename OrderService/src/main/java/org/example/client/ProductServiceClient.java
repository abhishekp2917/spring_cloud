package org.example.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.dto.APIResponseDTO;
import org.example.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Feign client interface for communicating with the Product Microservice.
 * This interface uses Spring Cloud OpenFeign to create a declarative HTTP client for accessing
 * product-related endpoints in the product microservice. It abstracts away the details of
 * HTTP communication and allows interaction with the product microservice using Java method calls.
 */
@FeignClient(name = "product-ms", configuration = ProductServiceClientConfig.class)  // Indicates that this interface is a Feign client for the 'product-ms' service.
public interface ProductServiceClient {

    /**
     * Retrieves a list of products filtered by category name from the product microservice.
     * This method is used to fetch product data based on the specified category.
     *
     * @param category The category name to filter products.
     * @return An APIResponseDTO containing a list of ProductDTO objects representing the products
     *         in the specified category.
     */
    @GetMapping("/product/category")  // Maps the method to the '/product/category' GET endpoint of the product microservice.
    @CircuitBreaker(name = "product-ms", fallbackMethod = "getProductByCategoryNameFallback")
    @Retry(name = "product-ms")
    public APIResponseDTO<List<ProductDTO>> getProductByCategoryName(@RequestParam(name = "category") String category);

    /**
     * Fallback method that is called when the primary method fails or the Circuit Breaker is open.
     * This method is responsible for providing a default response in case of errors when calling
     * the getProductByCategoryName method.
     *
     * The required signature for a fallback method is as follows:
     * - It must have the same return type as the original method (APIResponseDTO<List<ProductDTO>> in this case).
     * - It must accept the same parameters as the original method, followed by an additional Throwable parameter.
     *
     * @param category The category name that was used in the original request.
     * @param exception The Throwable that caused the fallback to be invoked.
     * @return An APIResponseDTO with a default response indicating an error.
     */
    default APIResponseDTO<List<ProductDTO>> getProductByCategoryNameFallback(String category, Throwable exception) {
        ProductDTO productDTO = ProductDTO.builder()
                                    .category(category)
                                    .build();
        List<ProductDTO> products = new ArrayList<>();
        products.add(productDTO);
        APIResponseDTO<List<ProductDTO>> response = new APIResponseDTO<>();
        response.setStatus(200);
        response.setBody(products);
        return response;
    }
}
