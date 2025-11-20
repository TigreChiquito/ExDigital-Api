package Ex.Digital.Api.ExdigitalApi.service;

import Ex.Digital.Api.ExdigitalApi.dto.CategoryDto;
import Ex.Digital.Api.ExdigitalApi.dto.CreateCategoryRequest;
import Ex.Digital.Api.ExdigitalApi.entity.Category;
import Ex.Digital.Api.ExdigitalApi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return convertToDto(category);
    }

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategory(Integer id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getIdCategories(),
                category.getName(),
                category.getDescription()
        );
    }
}