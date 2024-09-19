package org.example.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.InternalServerErrorException;
import org.example.dto.APIResponseDTO;
import org.example.dto.CategoryDTO;
import org.example.dto.ProductDTO;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.UtbCategory;
import org.example.model.UtbProduct;
import org.example.service.CategoryServices;
import org.example.service.ProductServices;
import org.example.utility.DTOMapperUtil;
import org.example.utility.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing product-related operations.
 * This controller provides endpoints for creating, retrieving, and managing products.
 * It also includes utility endpoints for application health and environment checks.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @Autowired
    private CategoryServices categoryServices;

    @Autowired
    private Environment environment;
    // Provides access to application environment properties, such as server port, which can be useful for diagnostics and configuration.

    /**
     * Endpoint to check the status of the application.
     * This endpoint returns a simple "Active" message indicating that the application is running.
     * Useful for basic health checks or status monitoring.
     *
     * @return A status message indicating the application is active.
     */
    @ApiOperation(value = "Check Application Status", notes = "Returns 'Active' if the application is running")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping("/status")
    public String getStatus() {
        return "Active";  // Hardcoded status message for monitoring application health.
    }

    /**
     * Endpoint to retrieve the server port from the environment properties.
     * This can be useful for debugging or configuration purposes, especially in environments where the port may change.
     *
     * @return The server port configured in application properties.
     */
    @ApiOperation(value = "Retrieve Server Port", notes = "Returns the server port from the environment properties")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/env")
    public String getEnvironment() {
        return environment.getProperty("server.port");
        // Fetches the server port from environment properties for diagnostic purposes.
    }

    /**
     * Endpoint to retrieve a product by its ID.
     * If the product exists, it returns the product details; otherwise, it throws a ResourceNotFoundException.
     *
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity containing the APIResponseDTO with the product details.
     */
    @ApiOperation(value = "Get Product by ID", notes = "Retrieves a product based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO> getProductById(@PathVariable(name = "id") long id) {
        UtbProduct product = productServices.findByProductId(id);
        if(product != null) {
            ProductDTO productDTO = DTOMapperUtil.toProductDTO(product);
            return ResponseUtil.buildSuccessResponse(HttpStatus.OK, productDTO);
        } else {
            throw new ResourceNotFoundException(String.format("No Product with the product ID - %s found", id));
        }
    }

    /**
     * Endpoint to create a new product.
     * If the product is successfully created, it returns the product details; otherwise, it throws an InternalServerErrorException.
     *
     * @param product The product entity to be created.
     * @return A ResponseEntity containing the APIResponseDTO with the created product details.
     */
    @ApiOperation(value = "Create New Product", notes = "Creates a new product and returns the created product details")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product created successfully"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("/create")
    public ResponseEntity<APIResponseDTO> createProduct(@RequestBody UtbProduct product) {
        product = productServices.save(product);
        if(product != null && product.getId() != null) {
            ProductDTO productDTO = DTOMapperUtil.toProductDTO(product);
            return ResponseUtil.buildSuccessResponse(HttpStatus.CREATED, productDTO);
        } else {
            throw new InternalServerErrorException("Unable to create product");
        }
    }

    /**
     * Endpoint to retrieve products by category.
     * If products exist in the specified category, it returns the list of products; otherwise, it throws a ResourceNotFoundException.
     *
     * @param categoryName The name of the category to filter products by.
     * @return A ResponseEntity containing the APIResponseDTO with the list of product details.
     */
    @ApiOperation(value = "Get Products by Category", notes = "Retrieves products based on the provided category name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "No products found in this category")
    })
    @GetMapping("/category")
    public ResponseEntity<APIResponseDTO> getProductByCategory(@RequestParam(name = "category") String categoryName) {
        List<UtbProduct> products = productServices.findByProductCategory(categoryName);
        if(products != null && !products.isEmpty()) {
            List<ProductDTO> productDTOS = products.stream()
                    .map(DTOMapperUtil::toProductDTO)
                    .toList();
            return ResponseUtil.buildSuccessResponse(HttpStatus.OK, productDTOS);
        } else {
            throw new ResourceNotFoundException(String.format("No Product with the category - %s found", categoryName));
        }
    }

    /**
     * Creates a new product category.
     * <p>
     * This method accepts a {@link UtbCategory} object in the request body and attempts to create a new product category.
     * It first checks if the category name already exists using the {@link CategoryServices#findByCategoryName(String)} method.
     * If a category with the same name exists, it throws a {@link BadRequestException} indicating a conflict.
     * If no such category exists, it saves the new category using {@link CategoryServices#save(UtbCategory)}.
     * <p>
     * If the category is successfully created, it returns an HTTP 201 Created response along with the newly created category data.
     * Otherwise, an {@link InternalServerErrorException} is thrown, indicating that the category creation process failed.
     *
     * @param category The category object to be created, provided in the request body.
     * @return A {@link ResponseEntity} containing the created category and HTTP status 201 (Created).
     * @throws BadRequestException if the category name already exists.
     * @throws InternalServerErrorException if there is an issue saving the category.
     */
    @ApiOperation(value = "Create a new product category",
            notes = "Creates a new product category if it does not already exist in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Category created successfully"),
            @ApiResponse(code = 400, message = "Category already exists"),
            @ApiResponse(code = 500, message = "Internal server error, unable to create the category")
    })
    @PostMapping("/category/create")
    public ResponseEntity<APIResponseDTO> createProductCategory(@RequestBody UtbCategory category) {
        if(category!=null && categoryServices.findByCategoryName(category.getName())!=null) {
            throw new BadRequestException(String.format("Category : %s already exists", category.getName()));
        }
        category = categoryServices.save(category);
        if(category != null && category.getId() != null) {
            CategoryDTO categoryDTO = DTOMapperUtil.toCategoryDTO(category);
            return ResponseUtil.buildSuccessResponse(HttpStatus.CREATED, categoryDTO);
        } else {
            throw new InternalServerErrorException("Unable to create product category");
        }
    }
}
