import { Component, OnInit } from '@angular/core';
import { DeviceService } from '../../controller/service/device.service';
import { Device } from "../../controller/model/Device";
import { Category } from "../../controller/model/Category";
import { MessageService } from 'primeng/api';
import { compareWithIds } from "../../controller/utils/global-method";

/**
 * Component for displaying and managing the list of devices.
 */
@Component({
    selector: 'app-list-device',
    templateUrl: './list-device.component.html',
})
export class ListDeviceComponent implements OnInit {
    displaySaveDialog = false;
    devices: Device[] = [];
    categories: Category[] = [];

    newDevice: Device = new Device();

    constructor(private deviceService: DeviceService, private messageService: MessageService) { }

    ngOnInit(): void {
        this.findAll();
        this.findAllCategories();
    }

    /**
     * Fetches all devices from the service.
     */
    findAll(): void {
        this.deviceService.findAll().subscribe(
            (data) => {
                this.devices = data;
            },
            (error) => {
                console.error('Error fetching devices:', error);
            }
        );
    }

    /**
     * Fetches all categories from the service.
     */
    findAllCategories(): void {
        this.deviceService.getCategories().subscribe(
            (data) => {
                this.categories = data;
            },
            (error) => {
                console.error('Error fetching categories:', error);
            }
        );
    }

    /**
     * Opens the dialog for adding a new device.
     */
    openSaveDialog(): void {
        this.displaySaveDialog = true;
    }

    /**
     * Handles file selection for the device photo.
     *
     * @param event The file selection event
     */
    onFileSelect(event: any): void {
        const file = event.files && event.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                if (typeof reader.result === 'string') {
                    this.newDevice.photo = reader.result;
                }
            };
            reader.readAsDataURL(file);
        }
    }

    /**
     * Saves the new device to the backend.
     */
    save(): void {
        console.log("Saving device:", this.newDevice);
        this.deviceService.saveDevice(this.newDevice).subscribe(
            (response) => {
                this.devices.unshift({ ...response });
                this.displaySaveDialog = false;
                this.showUpdateToast("Device added successfully", "success", "Done");
                // Reset new device
                this.newDevice = new Device();
            },
            (error) => {
                this.showUpdateToast(error?.error?.message || "Something went wrong", "error", "Error");
            }
        );
    }

    /**
     * Displays a toast message for updates.
     *
     * @param message The message to display
     * @param severity The severity level (success, error, etc.)
     * @param summary The summary text
     */
    private showUpdateToast(message: string, severity: string, summary: string): void {
        this.messageService.add({ severity: severity, summary: summary, detail: message });
    }

    /**
     * Deletes a device by its identifier.
     *
     * @param id Identifier of the device to delete
     */
    delete(id: any): void {
        this.deviceService.deleteDevice(id).subscribe(
            () => {
                this.findAll();
            },
            (error) => {
                console.error('Error deleting device:', error);
            }
        );
    }

    /**
     * Updates an existing device's state.
     *
     * @param device The device to update
     */
    update(device: Device): void {
        const updatedDevice: { id?: number; state?: boolean } = {
            id: device.id,
            state: device.state,
        };
        this.deviceService.updateDevice(device.id, updatedDevice).subscribe(
            (response) => {
                console.log("Device state updated successfully:", response);
            },
            (error) => {
                console.error('Error updating device state:', error);
            }
        );
    }

    /**
     * Switches the state of all devices.
     *
     * @param state The new state to apply
     */
    switchOnOrOff(state: boolean): void {
        this.deviceService.switchState(state).subscribe(
            () => {
                this.findAll();
            },
            (error) => {
                console.error('Error switching all devices state:', error);
            }
        );
    }

    protected readonly compareWithIds = compareWithIds;
}
