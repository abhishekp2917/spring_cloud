package org.example.controller;

import org.example.client.ProductServiceClient;
import org.example.dto.APIResponseDTO;
import org.example.dto.ProductDTO;
import org.example.service.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderServices orderServices;

    @Autowired
    ProductServiceClient productServiceClient;

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "id") long id) {
        ProductDTO product = orderServices.getProductById(id);
        if(product!=null) {
            return new ResponseEntity<ProductDTO>(product, HttpStatus.OK);
        }
        else return null;
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductDTO>> getProductByCategoryName(@RequestParam(name = "category") String category) {
        APIResponseDTO<List<ProductDTO>> response = productServiceClient.getProductByCategoryName(category);
        if(response.getBody()!=null) {
            return new ResponseEntity<List<ProductDTO>>(response.getBody(), HttpStatus.OK);
        }
        else return null;
    }
}
