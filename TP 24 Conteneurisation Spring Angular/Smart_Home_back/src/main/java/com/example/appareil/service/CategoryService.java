package com.example.appareil.service;

import com.example.appareil.entity.Category;
import com.example.appareil.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Category operations.
 *
 * <p>
 * This class provides business logic for category management, including
 * retrieving all categories, finding a category by ID, and saving new ones.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Retrieves all categories from the database.
     *
     * @return List of all categories
     */
    public List<Category> findAll() {
        logger.info("Fetching all categories from the database.");
        return categoryRepository.findAll();
    }

    /**
     * Saves a new category or updates an existing one.
     *
     * @param category The category to save
     * @return The saved category
     */
    public Category save(Category category) {
        logger.info("Saving category: {}", category.getLabel());
        return categoryRepository.save(category);
    }

    /**
     * Retrieves a category by its identifier.
     *
     * @param id Identifier of the category
     * @return An Optional containing the category if found
     */
    public Optional<Category> findById(Long id) {
        logger.info("Fetching category with ID: {}", id);
        return categoryRepository.findById(id);
    }

    /**
     * Deletes a category by its identifier.
     *
     * @param id Identifier of the category to delete
     */
    public void deleteById(Long id) {
        logger.info("Deleting category with ID: {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Updates an existing category's information.
     *
     * @param id      Identifier of the category to update
     * @param catInfo New category information
     * @throws ResourceNotFoundException If the category is not found
     */
    public void update(Long id, Category catInfo) {
        logger.info("Updating category with ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });
        category.setLabel(catInfo.getLabel());
        categoryRepository.save(category);
    }
}
