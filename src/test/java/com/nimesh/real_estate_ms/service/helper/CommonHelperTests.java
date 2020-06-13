package com.nimesh.real_estate_ms.service.helper;

import com.nimesh.real_estate_ms.config.AppConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommonHelperTests {

    @Test
    public void testRoundCoordinatesToMaxDecimalPlaces() {
        Double delta = Math.pow(0.1, AppConstants.COORDINATE_MAX_DECIMALS + 1);
        Double a = 123.1234444;
        Double b = CommonHelper.roundCoordinatesToMaxDecimalPlaces(a);
        assertEquals(b, 123.123444, delta);
        a = 123.1234445;
        b = CommonHelper.roundCoordinatesToMaxDecimalPlaces(a);
        assertEquals(b, 123.123445, delta);
        a = 123.1234455;
        b = CommonHelper.roundCoordinatesToMaxDecimalPlaces(a);
        assertEquals(b, 123.123446, delta);
        a = 123.1234456;
        b = CommonHelper.roundCoordinatesToMaxDecimalPlaces(a);
        assertEquals(b, 123.123446, delta);
    }

    @Test
    public void testRoundToTwoDecimalPlaces() {
        Double delta = Math.pow(0.1, 3);
        Double a = 123.124;
        Double b = CommonHelper.roundToTwoDecimalPlaces(a);
        assertEquals(b, 123.12, delta);
        a = 123.125;
        b = CommonHelper.roundToTwoDecimalPlaces(a);
        assertEquals(b, 123.13, delta);
        a = 123.135;
        b = CommonHelper.roundToTwoDecimalPlaces(a);
        assertEquals(b, 123.14, delta);
        a = 123.136;
        b = CommonHelper.roundToTwoDecimalPlaces(a);
        assertEquals(b, 123.14, delta);
        a = 12000000.0;
        b = CommonHelper.roundToTwoDecimalPlaces(a);
        assertEquals(b, 12000000.0, delta);
    }

    @Test
    public void testIsMultipleOfHalf() {
        Float a = 0.0f;
        assertTrue(CommonHelper.isMultipleOfHalf((double)a));
        a = 0.5f;
        assertTrue(CommonHelper.isMultipleOfHalf((double)a));
        a = 1.0f;
        assertTrue(CommonHelper.isMultipleOfHalf((double)a));
        a = 10.5f;
        assertTrue(CommonHelper.isMultipleOfHalf((double)a));
        a = 15.500001f;
        assertFalse(CommonHelper.isMultipleOfHalf((double)a));
        a = 20.499999f;
        assertFalse(CommonHelper.isMultipleOfHalf((double)a));
        a = Float.valueOf("5.5");
        assertTrue(CommonHelper.isMultipleOfHalf((double)a));
    }
}
