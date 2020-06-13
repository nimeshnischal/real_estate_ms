package com.nimesh.real_estate_ms.controller;

import com.nimesh.real_estate_ms.config.AppConstants;
import com.nimesh.real_estate_ms.controller.helper.ControllerHelper;
import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.exception.NotFoundException;
import com.nimesh.real_estate_ms.service.PropertyService;
import com.nimesh.real_estate_ms.service.RequirementPropertyMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;

    private final RequirementPropertyMatchService requirementPropertyMatchService;

    PropertyController(PropertyService propertyService, RequirementPropertyMatchService requirementPropertyMatchService) {
        this.propertyService = propertyService;
        this.requirementPropertyMatchService = requirementPropertyMatchService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Property property) {
        propertyService.save(property);
        Map<String, Object> response = ControllerHelper.pageToMap(
                requirementPropertyMatchService.getMatchingRequirements(property, 0, 10),
                "requirements");
        response.put("property_id", property.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/requirements", method = RequestMethod.GET)
    public ResponseEntity getMatchingRequirements(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") Integer offset,
                                                  @RequestParam(defaultValue = "10") Integer page_size) {
        Property property = propertyService.getById(id);
        if (property == null)
            throw new NotFoundException("Property not found for id: " + id);
        Map response = ControllerHelper.pageToMap(
                requirementPropertyMatchService.getMatchingRequirements(property, offset, page_size), "requirements");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
