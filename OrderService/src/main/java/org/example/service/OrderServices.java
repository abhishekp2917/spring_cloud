package org.example.service;

import org.example.dto.APIResponseDTO;
import org.example.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class responsible for handling operations related to orders.
 * This class interacts with external services to fetch product details using RestTemplate.
 */
@Service  // Indicates that this class is a Spring service component for business logic.
public class OrderServices {

    @Autowired
    RestTemplate restTemplate;  // Injected RestTemplate for making HTTP requests.

    @Autowired
    Environment environment;  // Injected Environment to access application properties.

    /**
     * Retrieves a product by its ID by making an HTTP GET request to an external service.
     * The URL for the request is constructed using a property value and the provided product ID.
     *
     * @param id The ID of the product to be retrieved.
     * @return The ProductDTO object containing product details if the request is successful;
     *         otherwise, returns null.
     */
    public ProductDTO getProductById(long id) {
        // Construct the endpoint URL for the product service using the ID and environment property.
        String endpoint = String.format(environment.getProperty("url.product.get.getProductById"), id);
        // Define the type reference for the response to handle generic types.
        ParameterizedTypeReference<APIResponseDTO<ProductDTO>> reference = new ParameterizedTypeReference<APIResponseDTO<ProductDTO>>() {};
        // Make an HTTP GET request to the external service to retrieve the product details.
        ResponseEntity<APIResponseDTO<ProductDTO>> response = restTemplate.exchange(endpoint, HttpMethod.GET, null, reference);
        // Extract the response body and product details if the response status is OK.
        APIResponseDTO<ProductDTO> responseBody = response.getBody();
        ProductDTO product = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            product = responseBody.getBody();
        }
        // Return the retrieved product or null if the request was unsuccessful.
        return product;
    }
}
