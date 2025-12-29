package com.example.appareil.cotroller;

import com.example.appareil.entity.Category;
import com.example.appareil.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Category management.
 *
 * <p>
 * This class exposes REST endpoints for category operations. It uses
 * the CategoryService for business logic.
 * </p>
 *
 * <p>
 * Exposed Endpoints:
 * </p>
 * <ul>
 * <li>GET /api/controller/category/: Retrieves all categories</li>
 * <li>GET /api/controller/category/id/{id}: Retrieves a category by ID</li>
 * <li>POST /api/controller/category/: Adds a new category</li>
 * <li>PUT /api/controller/category/id/{id}: Updates a category</li>
 * <li>DELETE /api/controller/category/id/{id}: Deletes a category</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/controller/category")
@CrossOrigin
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return List of all categories
     */
    @GetMapping("/")
    public List<Category> findAll() {
        logger.info("REST request to fetch all categories.");
        return categoryService.findAll();
    }

    /**
     * Adds a new category.
     *
     * @param category The category to add
     * @return The saved category
     */
    @PostMapping("/")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        logger.info("REST request to save new category: {}", category.getLabel());
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id Identifier of the category
     * @return The category if found
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        logger.info("REST request to fetch category with ID: {}", id);
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id Identifier of the category to delete
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("REST request to delete category with ID: {}", id);
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates a category by its ID.
     *
     * @param id      Identifier of the category to update
     * @param catInfo New category information
     */
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Category catInfo) {
        logger.info("REST request to update category with ID: {}", id);
        categoryService.update(id, catInfo);
        return ResponseEntity.ok().build();
    }
}
