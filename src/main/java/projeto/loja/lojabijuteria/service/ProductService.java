package projeto.loja.lojabijuteria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto.loja.lojabijuteria.dto.ProductDTO;
import projeto.loja.lojabijuteria.entity.Category;
import projeto.loja.lojabijuteria.entity.Product;
import projeto.loja.lojabijuteria.repository.CategoryRepository;
import projeto.loja.lojabijuteria.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private ProductDTO toDTO(Product product){
        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setActive(product.getActive());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());

        return dto;
    }

    public Page<ProductDTO> findAll(Pageable pageable){
        return productRepository.findByActiveTrue(pageable)
                .map(this::toDTO);
    }

    public Page<ProductDTO> findByCategory(Long categoryId, Pageable pageable){
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable)
                .map(this::toDTO);
    }

    public ProductDTO findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Produto nao encontrado")    );
        return toDTO(product);
    }

    public ProductDTO create(ProductDTO dto){
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada") );

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        product.setActive(true);

        return toDTO(productRepository.save(product));
    }

    public void delete(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado")   );

        product.setActive(false);
        productRepository.save(product);
    }
}
