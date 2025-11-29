package Ex.Digital.Api.ExdigitalApi.service;

import Ex.Digital.Api.ExdigitalApi.dto.CreateProductRequest;
import Ex.Digital.Api.ExdigitalApi.dto.ProductDto;
import Ex.Digital.Api.ExdigitalApi.dto.UpdateProductRequest;
import Ex.Digital.Api.ExdigitalApi.entity.Category;
import Ex.Digital.Api.ExdigitalApi.entity.Discount;
import Ex.Digital.Api.ExdigitalApi.entity.Product;
import Ex.Digital.Api.ExdigitalApi.entity.ProductImages;
import Ex.Digital.Api.ExdigitalApi.repository.CategoryRepository;
import Ex.Digital.Api.ExdigitalApi.repository.DiscountRepository;
import Ex.Digital.Api.ExdigitalApi.repository.ProductImagesRepository;
import Ex.Digital.Api.ExdigitalApi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductImagesRepository productImagesRepository;

    // Usamos la query optimizada aquí
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllWithRelations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getActiveProducts() {
        return productRepository.findByIsActive(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAvailableProducts() {
        return productRepository.findAvailableProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertToDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryIdCategories(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String keyword) {
        return productRepository.searchByName(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product product = new Product();
        product.setName(request.getName());
        product.setValue(request.getValue());
        product.setCategory(category);
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
        product.setIsActive(true);

        if (request.getDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
            product.setDiscount(discount);
        }

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Integer id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getValue() != null) {
            product.setValue(request.getValue());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
            product.setDiscount(discount);
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Transactional
    public void updateStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new RuntimeException("Stock insuficiente");
        }

        product.setStock(newStock);
        productRepository.save(product);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setIdProduct(product.getIdProduct());
        dto.setName(product.getName());
        dto.setValue(new BigDecimal(product.getValue()));
        dto.setCategoryName(product.getCategory().getName());
        dto.setCategoryId(product.getCategory().getIdCategories());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setIsActive(product.getIsActive());

        // Calcular precio final con descuento
        BigDecimal finalPrice = new BigDecimal(product.getValue());
        if (product.getDiscount() != null && product.getDiscount().getActive()) {
            dto.setDiscount(product.getDiscount().getDiscount());
            dto.setDiscountName(product.getDiscount().getName());
            
            BigDecimal discountAmount = finalPrice
                    .multiply(product.getDiscount().getDiscount())
                    .divide(new BigDecimal("100"));
            finalPrice = finalPrice.subtract(discountAmount);
        }
        dto.setFinalPrice(finalPrice);

        // OPTIMIZACIÓN: Usar la lista de imágenes ya cargada en la entidad
        // en lugar de llamar al repositorio nuevamente.
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            List<String> imageUrls = product.getImages().stream()
                    .sorted(Comparator.comparingInt(ProductImages::getOrderPosition))
                    .map(ProductImages::getUrl)
                    .collect(Collectors.toList());
            
            dto.setImageUrls(imageUrls);
            
            product.getImages().stream()
                    .filter(ProductImages::getIsPrimary)
                    .findFirst()
                    .ifPresent(img -> dto.setPrimaryImage(img.getUrl()));
        }

        return dto;
    }
}