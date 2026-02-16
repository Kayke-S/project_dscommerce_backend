package com.kayke.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kayke.dscommerce.dto.ProductDTO;
import com.kayke.dscommerce.entities.Product;
import com.kayke.dscommerce.repositories.ProductRepository;
import com.kayke.dscommerce.services.exceptions.DatabaseException;
import com.kayke.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));

        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {

        Page<Product> result = productRepository.findAll(pageable);

        return result.map(product -> new ProductDTO(product));
    }

    @Transactional()
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        product.fromDTO(productDTO);

        product = productRepository.save(product);

        return new ProductDTO(product);

    }

    @Transactional()
    public ProductDTO update(Long id, ProductDTO productDTO) { // ! n entendi mt bem
        try {
            Product product = productRepository.getReferenceById(id); // ! n entendi mt bem

            product.fromDTO(productDTO);

            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }
}
