package com.nimesh.real_estate_ms.controller;

import com.nimesh.real_estate_ms.controller.helper.ControllerHelper;
import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.exception.NotFoundException;
import com.nimesh.real_estate_ms.service.RequirementPropertyMatchService;
import com.nimesh.real_estate_ms.service.RequirementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/requirement")
public class RequirementController {

    private final RequirementService requirementService;

    private final RequirementPropertyMatchService requirementPropertyMatchService;

    RequirementController(RequirementService requirementService,
                          RequirementPropertyMatchService requirementPropertyMatchService) {
        this.requirementService = requirementService;
        this.requirementPropertyMatchService = requirementPropertyMatchService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Requirement requirement) {
        requirementService.save(requirement);
        Map<String, Object> response = ControllerHelper.pageToMap(
                requirementPropertyMatchService.getMatchingProperties(requirement, 0, 10),
                "properties");
        response.put("requirement_id", requirement.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/properties", method = RequestMethod.GET)
    public ResponseEntity getMatchingProperties(@PathVariable Long id,
                                                @RequestParam(defaultValue = "0") Integer offset,
                                                @RequestParam(defaultValue = "10") Integer page_size) {
        Requirement requirement = requirementService.getById(id);
        if (requirement == null)
            throw new NotFoundException("Requirement not found for id: " + id);
        Map response = ControllerHelper.pageToMap(
                requirementPropertyMatchService.getMatchingProperties(requirement, offset, page_size), "properties");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
