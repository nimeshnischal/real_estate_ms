package com.nimesh.real_estate_ms.repository;

import com.nimesh.real_estate_ms.entity.Property;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PropertyRepository extends PropertyCustomRepository, CrudRepository<Property, Long> {
    List<Property> getAllByNameAndLatitudeAndLongitude(String name, Double latitude, Double longitude);
}
