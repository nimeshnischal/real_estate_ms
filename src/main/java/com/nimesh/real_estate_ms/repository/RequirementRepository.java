package com.nimesh.real_estate_ms.repository;

import com.nimesh.real_estate_ms.entity.Requirement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequirementRepository extends RequirementCustomRepository, CrudRepository<Requirement, Long> {
    List<Requirement> getAllByLatitudeAndLongitudeAndMinBudgetAndMaxBudgetAndMinBedroomsAndMaxBedroomsAndMinBathroomsAndMaxBathrooms(
            Double latitude, Double longitude, Double minBudget, Double maxBudget, Double minBedrooms, Double maxBedrooms,
            Integer minBathrooms, Integer maxBathrooms);
}
