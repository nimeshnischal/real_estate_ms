package com.nimesh.real_estate_ms.service;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.repository.PropertyRepository;
import com.nimesh.real_estate_ms.repository.RequirementRepository;
import com.nimesh.real_estate_ms.repository.util.CustomPage;
import com.nimesh.real_estate_ms.service.helper.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequirementPropertyMatchService {

    private final RequirementRepository requirementRepository;

    private final PropertyRepository propertyRepository;

    @Autowired
    public RequirementPropertyMatchService(RequirementRepository requirementRepository,
                                           PropertyRepository propertyRepository) {
        this.requirementRepository = requirementRepository;
        this.propertyRepository = propertyRepository;
    }

    public CustomPage<Requirement> getMatchingRequirements(Property property, Integer offset, Integer pageSize) {
        CommonHelper.validateOffsetAndPageSize(offset, pageSize);
        return requirementRepository.findAllByProperty(property, offset, pageSize);
    }

    public CustomPage<Property> getMatchingProperties(Requirement requirement, Integer offset, Integer pageSize) {
        CommonHelper.validateOffsetAndPageSize(offset, pageSize);
        return propertyRepository.findAllByRequirement(requirement, offset, pageSize);
    }
}
