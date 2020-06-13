package com.nimesh.real_estate_ms.service.helper;

import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.exception.InvalidInputException;
import com.nimesh.real_estate_ms.repository.RequirementRepository;
import org.springframework.stereotype.Component;

@Component
public class RequirementServiceHelper {

//    private final RequirementRepository requirementRepository;
//
//    public RequirementServiceHelper(RequirementRepository requirementRepository) {
//        this.requirementRepository = requirementRepository;
//    }

    public void validateCreate(Requirement requirement) {
        CommonHelper.validateLatitude(requirement.getLatitude());
        CommonHelper.validateLongitude(requirement.getLongitude());

        if (requirement.getMinBudget() == null && requirement.getMaxBudget() == null)
            throw new InvalidInputException("Either min budget or max budget is required");
        if (requirement.getMinBudget() != null && requirement.getMinBudget() < 0)
                throw new InvalidInputException("Expected min budget more than or equal to 0: " + requirement.getMinBudget());
        if (requirement.getMaxBudget() != null && requirement.getMaxBudget() < 0.01)
            throw new InvalidInputException("Expected max budget more than 0: " + requirement.getMaxBudget());
        if (requirement.getMinBudget() != null && requirement.getMaxBudget() != null &&
                requirement.getMaxBudget() < requirement.getMinBudget())
            throw new InvalidInputException("Max budget (" + requirement.getMaxBudget() +
                    ") cannot be less than  min budget (" + requirement.getMinBudget() + ")");

        if (requirement.getMinBedrooms() == null && requirement.getMaxBedrooms() == null)
            throw new InvalidInputException("Either min bedrooms or max bedrooms is required");
        if (requirement.getMinBedrooms() != null)
            if (requirement.getMinBedrooms() < 0)
                throw new InvalidInputException("Expected min bedrooms more than or equal to 0: " + requirement.getMinBedrooms());
            else if (!CommonHelper.isMultipleOfHalf((double)requirement.getMinBedrooms()))
                throw new InvalidInputException("Expected min bedrooms to be a whole number or multiple of 0.5: "
                        + requirement.getMinBedrooms());
        if (requirement.getMaxBedrooms() != null)
            if (requirement.getMaxBedrooms() < 0.5)
                throw new InvalidInputException("Expected max bedrooms more than 0.5: " + requirement.getMaxBedrooms());
            else if (!CommonHelper.isMultipleOfHalf((double)requirement.getMaxBedrooms()))
                throw new InvalidInputException("Expected max bedrooms to be a whole number or multiple of 0.5: "
                        + requirement.getMaxBedrooms());
        if (requirement.getMinBedrooms() != null && requirement.getMaxBedrooms() != null &&
                requirement.getMaxBedrooms() < requirement.getMinBedrooms())
            throw new InvalidInputException("Max bedrooms (" + requirement.getMaxBedrooms() +
                    ") cannot be less than  min bedrooms (" + requirement.getMinBedrooms() + ")");

        if (requirement.getMinBathrooms() == null && requirement.getMaxBathrooms() == null)
            throw new InvalidInputException("Either min bathrooms or max bathrooms is required");
        if (requirement.getMinBathrooms() != null && requirement.getMinBathrooms() < 0)
            throw new InvalidInputException("Expected min bathrooms more than or equal to 0: " + requirement.getMinBathrooms());
        if (requirement.getMaxBathrooms() != null && requirement.getMaxBathrooms() <= 0)
            throw new InvalidInputException("Expected max bathrooms more than 0: " + requirement.getMaxBathrooms());
        if (requirement.getMinBathrooms() != null && requirement.getMaxBathrooms() != null &&
                requirement.getMaxBathrooms() < requirement.getMinBathrooms())
            throw new InvalidInputException("Max bathrooms (" + requirement.getMaxBathrooms() +
                    ") cannot be less than  min bathrooms (" + requirement.getMinBathrooms() + ")");
    }

    public void clean(Requirement requirement) {
        requirement.setLatitude(CommonHelper.roundCoordinatesToMaxDecimalPlaces(requirement.getLatitude()));
        requirement.setLongitude(CommonHelper.roundCoordinatesToMaxDecimalPlaces(requirement.getLongitude()));
        if (requirement.getMinBudget() != null)
            requirement.setMinBudget(CommonHelper.roundToTwoDecimalPlaces(requirement.getMinBudget()));
        if (requirement.getMaxBudget() != null)
            requirement.setMaxBudget(CommonHelper.roundToTwoDecimalPlaces(requirement.getMaxBudget()));
        populateOptionalFields(requirement);
        requirement.setMeanBudget(CommonHelper.getMean(requirement.getMinBudget(), requirement.getMaxBudget()));
        requirement.setRangeBudget(CommonHelper.getNonZeroRange(requirement.getMinBudget(), requirement.getMaxBudget()));
    }

    private void populateOptionalFields(Requirement requirement) {
        if (requirement.getMinBudget() == null)
            requirement.setMinBudget(requirement.getMaxBudget());
        if (requirement.getMaxBudget() == null)
            requirement.setMaxBudget(requirement.getMinBudget());
        if (requirement.getMinBedrooms() == null)
            requirement.setMinBedrooms(requirement.getMaxBedrooms());
        if (requirement.getMaxBedrooms() == null)
            requirement.setMaxBedrooms(requirement.getMinBedrooms());
        if (requirement.getMinBathrooms() == null)
            requirement.setMinBathrooms(requirement.getMaxBathrooms());
        if (requirement.getMaxBathrooms() == null)
            requirement.setMaxBathrooms(requirement.getMinBathrooms());
    }

//    public Requirement fetchSameRequirementIfExists(Requirement requirement) {
//        List<Requirement> sameRequirements =
//                requirementRepository.getAllByLatitudeAndLongitudeAndMinBudgetAndMaxBudgetAndMinBedroomsAndMaxBedroomsAndMinBathroomsAndMaxBathrooms(
//                        requirement.getLatitude(), requirement.getLongitude(), requirement.getMinBudget(),
//                        requirement.getMaxBudget(), requirement.getMinBedrooms(), requirement.getMaxBedrooms(),
//                        requirement.getMinBathrooms(), requirement.getMaxBathrooms());
//        if (sameRequirements != null && sameRequirements.size() > 0)
//            return sameRequirements.get(0);
//        return null;
//    }
}
