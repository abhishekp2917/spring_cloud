package org.example.client;

import org.example.dto.APIResponseDTO;
import org.example.dto.ProductDTO;

import java.util.List;

public class ProductServiceClientFallback implements ProductServiceClient {
    @Override
    public APIResponseDTO<List<ProductDTO>> getProductByCategoryName(String category) {
        APIResponseDTO<List<ProductDTO>> response = new APIResponseDTO<>();
        response.setStatus(503);
        response.setError("Service Unavailable");
        return response;
    }
}
