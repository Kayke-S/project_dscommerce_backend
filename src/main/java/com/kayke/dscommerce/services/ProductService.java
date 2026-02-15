package com.kayke.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kayke.dscommerce.dto.ProductDTO;
import com.kayke.dscommerce.entities.Product;
import com.kayke.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = productRepository.findById(id);

        Product product = result.get();

        return new ProductDTO(product);
    }

}
