package com.nimesh.real_estate_ms.repository;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.repository.util.CustomPage;

public interface PropertyCustomRepository {
    CustomPage<Property> findAllByRequirement(Requirement requirement, long offset, int pageSize);
}
