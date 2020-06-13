package com.nimesh.real_estate_ms.service.helper;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.exception.ConflictException;
import com.nimesh.real_estate_ms.exception.InvalidInputException;
import com.nimesh.real_estate_ms.repository.PropertyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.nimesh.real_estate_ms.config.AppConstants.PROPERTY_NAME_MIN_CHARACTERS;

@Component
public class PropertyServiceHelper {

    private  PropertyRepository propertyRepository;

    public PropertyServiceHelper(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public void validateCreate(Property property) {
        if (property.getName() == null)
            throw new InvalidInputException("Name not provided");
        if (property.getName().trim().length() < PROPERTY_NAME_MIN_CHARACTERS)
            throw new InvalidInputException("Invalid name: " + property.getName());
        CommonHelper.validateLatitude(property.getLatitude());
        CommonHelper.validateLongitude(property.getLongitude());
        if (property.getPrice() == null)
            throw new InvalidInputException("Price not provided");
        if (property.getPrice() < 0.01)
            throw new InvalidInputException("Expected price more than 0: " + property.getPrice());
        if (property.getBedroomCount() == null)
            throw new InvalidInputException("Bedroom count not provided");
        if (property.getBedroomCount() < 0.5)
            throw new InvalidInputException("Expected bedroom count more than 0: " + property.getBedroomCount());
        if (!CommonHelper.isMultipleOfHalf(property.getBedroomCount()))
            throw new InvalidInputException("Expected bedroom count to be a whole number or multiple of 0.5: "
                    + property.getBedroomCount());
        if (property.getBathroomCount() == null)
            throw new InvalidInputException("Bathroom count not provided");
        if (property.getBathroomCount() <= 0)
            throw new InvalidInputException("Expected bathroom count more than 0: " + property.getBathroomCount());
    }

    public void validateNotExistingProperty(Property property) {
        List<Property> existingSimilarProperties = propertyRepository.getAllByNameAndLatitudeAndLongitude(property.getName(),
                property.getLatitude(), property.getLongitude());
        if (existingSimilarProperties.size() > 0)
            throw new ConflictException("Similar property already exists with id(s): " +
                    existingSimilarProperties.stream().map(Property::getId).map(String::valueOf).collect(Collectors.joining(", ")));
    }

    public void clean(Property property) {
        property.setName(property.getName().trim());
        property.setLatitude(CommonHelper.roundCoordinatesToMaxDecimalPlaces(property.getLatitude()));
        property.setLongitude(CommonHelper.roundCoordinatesToMaxDecimalPlaces(property.getLongitude()));
        property.setPrice(CommonHelper.roundToTwoDecimalPlaces(property.getPrice()));
    }
}
