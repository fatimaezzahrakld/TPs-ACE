package com.example;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the App class.
 *
 * <p>
 * This class verifies the arithmetic operations provided by the App class.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
public class AppTest {

    /**
     * Tests the addition operation.
     */
    @Test
    public void testAdd() {
        App app = new App();
        assertEquals("2 + 3 should be 5", 5, app.add(2, 3));
    }

    /**
     * Tests the subtraction operation.
     */
    @Test
    public void testSubtract() {
        App app = new App();
        assertEquals("10 - 4 should be 6", 6, app.subtract(10, 4));
    }
}
