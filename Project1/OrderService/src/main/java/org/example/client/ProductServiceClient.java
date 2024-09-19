package org.example.client;

import org.example.dto.APIResponseDTO;
import org.example.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Feign client interface for communicating with the Product Microservice.
 * This interface uses Spring Cloud OpenFeign to create a declarative HTTP client for accessing
 * product-related endpoints in the product microservice. It abstracts away the details of
 * HTTP communication and allows interaction with the product microservice using Java method calls.
 */
@FeignClient(name = "product-ms")  // Indicates that this interface is a Feign client for the 'product-ms' service.
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
    public APIResponseDTO<List<ProductDTO>> getProductByCategoryName(@RequestParam(name = "category") String category);
}
