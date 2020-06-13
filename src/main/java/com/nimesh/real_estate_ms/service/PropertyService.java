package com.nimesh.real_estate_ms.service;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.exception.InvalidInputException;
import com.nimesh.real_estate_ms.repository.PropertyRepository;
import com.nimesh.real_estate_ms.service.helper.PropertyServiceHelper;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyServiceHelper propertyServiceHelper;

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyServiceHelper propertyServiceHelper, PropertyRepository propertyRepository) {
        this.propertyServiceHelper = propertyServiceHelper;
        this.propertyRepository = propertyRepository;
    }

    public Property save(Property property) {
        propertyServiceHelper.validateCreate(property);
        propertyServiceHelper.clean(property);
        propertyServiceHelper.validateNotExistingProperty(property);
        return propertyRepository.save(property);
    }

    public Property getById(Long id) {
        if (id == null || id < 0)
            throw new InvalidInputException("Invalid id: " + id);
        return propertyRepository.findById(id).orElse(null);
    }
}
