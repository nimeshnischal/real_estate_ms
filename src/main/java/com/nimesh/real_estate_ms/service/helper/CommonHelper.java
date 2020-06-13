package com.nimesh.real_estate_ms.service.helper;

import com.nimesh.real_estate_ms.config.AppConstants;
import com.nimesh.real_estate_ms.exception.InvalidInputException;

public class CommonHelper {
    static Double roundCoordinatesToMaxDecimalPlaces(Double coordinate) {
        double offset = Math.pow(10, AppConstants.COORDINATE_MAX_DECIMALS);
        return Math.round(coordinate * offset) / offset;
    }

    static Double roundToTwoDecimalPlaces(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    static Boolean isMultipleOfHalf(Double value) {
        return value % 0.5 == 0;
    }

    static void validateLatitude(Double latitude){
        if (latitude == null)
            throw new InvalidInputException("Latitude not provided");
        if (latitude < -90 || latitude > 90)
            throw new InvalidInputException("Invalid latitude: " + latitude);
    }
    
    static void validateLongitude(Double longitude) {
        if (longitude == null)
            throw new InvalidInputException("Longitude not provided");
        if (longitude < -180 || longitude > 180)
            throw new InvalidInputException("Invalid longitude: " + longitude);
    }

    public static Double getMean(Double minVal, Double maxVal) {
        return (maxVal + minVal)/2;
    }

    public static Double getRange(Double minVal, Double maxVal) {
        return (maxVal - minVal)/2;
    }

    public static Double getNonZeroRange(Double maxVal, Double minVal) {
        Double range = getRange(maxVal, minVal);
        return range == 0 ? maxVal * AppConstants.DEFAULT_FRACTION_MATCH : range;
    }

    public static void validateOffsetAndPageSize(Integer offset, Integer pageSize) {
        if (offset == null || offset < 0)
            throw new InvalidInputException("Invalid offset: " + offset);
        if (pageSize == null || pageSize <= 0)
            throw new InvalidInputException("Invalid pageSize: " + pageSize);
    }
}
