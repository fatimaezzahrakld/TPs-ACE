import { Category } from "./Category";

/**
 * Model class representing a smart Device in the system.
 */
export class Device {
    /**
     * Unique identifier for the device.
     */
    id?: number;

    /**
     * Label or name of the device.
     */
    label?: string;

    /**
     * Detailed description of the device.
     */
    description?: string;

    /**
     * Current state of the device (true for ON, false for OFF).
     */
    state?: boolean;

    /**
     * Base64 encoded photo or image path of the device.
     */
    photo?: string;

    /**
     * The category this device belongs to.
     */
    category?: Category;
}
