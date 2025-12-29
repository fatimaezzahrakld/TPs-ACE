import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Device } from "../model/Device";
import { Category } from "../model/Category";
import { environment } from "../../../environments/environment";

/**
 * Service for managing device-related operations.
 *
 * <p>This service provides methods to interact with the device and category
 * endpoints of the backend API.</p>
 */
@Injectable({
    providedIn: 'root'
})
export class DeviceService {
    private categoryUrl = environment.URL + "category";
    private deviceUrl = environment.URL + 'device';

    constructor(private http: HttpClient) { }

    /**
     * Retrieves all devices from the backend.
     *
     * @returns Observable of device array
     */
    findAll(): Observable<Device[]> {
        return this.http.get<Device[]>(`${this.deviceUrl}/`);
    }

    /**
     * Retrieves all categories from the backend.
     *
     * @returns Observable of category array
     */
    getCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(`${this.categoryUrl}/`);
    }

    /**
     * Saves a new device to the backend.
     *
     * @param device The device to save
     * @returns Observable of the saved device
     */
    saveDevice(device: Device): Observable<Device> {
        return this.http.post<Device>(`${this.deviceUrl}/`, device);
    }

    /**
     * Deletes a device by its identifier.
     *
     * @param id Identifier of the device to delete
     * @returns Observable of void
     */
    deleteDevice(id: number): Observable<void> {
        return this.http.delete<void>(`${this.deviceUrl}/id/${id}`);
    }

    /**
     * Updates an existing device's information.
     *
     * @param id Identifier of the device to update
     * @param device New device information
     * @returns Observable of the updated device
     */
    updateDevice(id: any, device: any): Observable<Device> {
        return this.http.put<Device>(`${this.deviceUrl}/id/${id}`, device);
    }

    /**
     * Switches the state of all devices in the system.
     *
     * @param state The new state to apply to all devices
     * @returns Observable of void
     */
    switchState(state: boolean): Observable<void> {
        return this.http.get<void>(`${this.deviceUrl}/update/state/${state}`);
    }
}
