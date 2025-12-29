import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Category } from "../model/Category";
import { environment } from "../../../environments/environment";

/**
 * Service for managing category-related operations.
 *
 * <p>This service provides methods to interact with the category
 * endpoints of the backend API.</p>
 */
@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    private apiUrl = environment.URL + 'category';

    constructor(private http: HttpClient) { }

    /**
     * Retrieves all categories from the backend.
     *
     * @returns Observable of category array
     */
    findAll(): Observable<Category[]> {
        return this.http.get<Category[]>(`${this.apiUrl}/`);
    }

    /**
     * Saves a new category to the backend.
     *
     * @param category The category to save
     * @returns Observable of the saved category
     */
    saveCategory(category: Category): Observable<Category> {
        return this.http.post<Category>(`${this.apiUrl}/`, category);
    }

    /**
     * Deletes a category by its identifier.
     *
     * @param id Identifier of the category to delete
     * @returns Observable of void
     */
    deleteCategory(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/id/${id}`);
    }

    /**
     * Updates an existing category's information.
     *
     * @param id Identifier of the category to update
     * @param updatedCategory New category information (label)
     * @returns Observable of the updated category
     */
    updateCategory(id: number, updatedCategory: { label: string }): Observable<Category> {
        return this.http.put<Category>(`${this.apiUrl}/id/${id}`, updatedCategory);
    }
}
