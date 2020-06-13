package com.nimesh.real_estate_ms.service;

import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.exception.InvalidInputException;
import com.nimesh.real_estate_ms.repository.RequirementRepository;
import com.nimesh.real_estate_ms.service.helper.RequirementServiceHelper;
import org.springframework.stereotype.Service;

@Service
public class RequirementService {

    private final RequirementServiceHelper requirementServiceHelper;

    private final RequirementRepository requirementRepository;

    public RequirementService(RequirementServiceHelper requirementServiceHelper,
                              RequirementRepository requirementRepository) {
        this.requirementServiceHelper = requirementServiceHelper;
        this.requirementRepository = requirementRepository;
    }

    public Requirement save(Requirement requirement) {
        requirementServiceHelper.validateCreate(requirement);
        requirementServiceHelper.clean(requirement);
//        Requirement existingSameRequirement = requirementServiceHelper.fetchSameRequirementIfExists(requirement);
//        if (existingSameRequirement != null)
//            return existingSameRequirement;
        return requirementRepository.save(requirement);
    }

    public Requirement getById(Long id) {
        if (id == null || id < 0)
            throw new InvalidInputException("Invalid id: " + id);
        return requirementRepository.findById(id).orElse(null);
    }
}
