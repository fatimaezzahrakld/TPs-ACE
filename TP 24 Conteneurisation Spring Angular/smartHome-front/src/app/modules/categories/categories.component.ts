import { Component, OnInit } from '@angular/core';
import { Category } from "../../controller/model/Category";
import { CategoryService } from "../../controller/service/category.service";

/**
 * Component for managing categories in the Smart Home system.
 */
@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  displaySaveDialog = false;
  displayUpdateDialog = false;

  newCategory: Category = {
    id: 0,
    label: '',
  };
  selectedCategory: Category = { id: 0, label: '' };

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  /**
   * Loads all categories from the service.
   */
  loadCategories(): void {
    this.categoryService.findAll().subscribe(
      (data) => {
        this.categories = data;
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }

  /**
   * Opens the dialog for saving a new category.
   */
  openSaveDialog(): void {
    this.displaySaveDialog = true;
  }

  /**
   * Opens the dialog for updating an existing category.
   *
   * @param category The category to update
   */
  openUpdateDialog(category: Category): void {
    this.selectedCategory = { ...category };
    this.displayUpdateDialog = true;
  }

  /**
   * Saves the new category to the backend.
   */
  saveCategory(): void {
    this.categoryService.saveCategory(this.newCategory).subscribe(
      (savedCategory) => {
        console.log("Category saved successfully:", savedCategory);
        this.displaySaveDialog = false;
        this.loadCategories();
        // Reset new category
        this.newCategory = { id: 0, label: '' };
      },
      (error) => {
        console.error('Error saving category:', error);
      }
    );
  }

  /**
   * Deletes a category by its ID.
   *
   * @param id Identifier of the category to delete
   */
  deleteCategory(id: number): void {
    this.categoryService.deleteCategory(id).subscribe(
      () => {
        this.loadCategories();
      },
      (error) => {
        console.error('Error deleting category:', error);
      }
    );
  }

  /**
   * Updates the selected category in the backend.
   */
  updateCategory(): void {
    this.categoryService.updateCategory(this.selectedCategory.id, { label: this.selectedCategory.label }).subscribe(
      () => {
        console.log("Category updated successfully");
        this.displayUpdateDialog = false;
        this.loadCategories();
      },
      (error) => {
        console.error('Error updating category:', error);
      }
    );
  }
}
