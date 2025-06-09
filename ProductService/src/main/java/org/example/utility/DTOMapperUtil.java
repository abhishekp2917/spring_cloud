package org.example.utility;

import org.example.dto.CategoryDTO;
import org.example.dto.ProductDTO;
import org.example.model.UtbCategory;
import org.example.model.UtbProduct;

/**
 * Utility class for mapping entity objects to Data Transfer Objects (DTOs).
 * This class centralizes the conversion logic from the UtbProduct entity to the ProductDTO,
 * ensuring consistent data transformation throughout the application.
 */
public class DTOMapperUtil {

    /**
     * Converts a UtbProduct entity to a ProductDTO.
     * This method simplifies the conversion process, which is useful for returning a DTO
     * in API responses, ensuring that the internal structure of entities remains hidden.
     *
     * @param product The UtbProduct entity that needs to be converted.
     * @return A ProductDTO object containing the mapped fields from the entity.
     */
    public static ProductDTO toProductDTO(UtbProduct product) {
        return ProductDTO.builder()
                .id(product.getId())  // Maps the product ID.
                .name(product.getName())  // Maps the product name.
                .price(product.getPrice())  // Maps the product price.
                .category(product.getCategory().getName())  // Maps the category name from the product's associated category.
                .description(product.getDescription())  // Maps the product description.
                .build();
    }

    /**
     * Converts a UtbCategory entity to a CategoryDTO.
     * This method simplifies the conversion process, which is useful for returning a DTO
     * in API responses, ensuring that the internal structure of entities remains hidden.
     *
     * @param category The UtbCategory entity that needs to be converted.
     * @return A CategoryDTO object containing the mapped fields from the entity.
     */
    public static CategoryDTO toCategoryDTO(UtbCategory category) {
        return CategoryDTO.builder()
                .id(category.getId())  // Maps the category ID.
                .name(category.getName())  // Maps the category name.
                .description(category.getDescription())  // Maps the category description.
                .build();
    }
}
