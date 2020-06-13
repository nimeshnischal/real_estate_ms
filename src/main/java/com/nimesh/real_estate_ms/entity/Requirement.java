package com.nimesh.real_estate_ms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="requirement")
public class Requirement extends BaseEntity {

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "min_budget", nullable = false)
    private Double minBudget;

    @Column(name = "max_budget", nullable = false)
    private Double maxBudget;

    @JsonIgnore
    @Column(name = "mean_budget", nullable = false)
    private Double meanBudget;

    @JsonIgnore
    @Column(name = "range_budget", nullable = false)
    private Double rangeBudget;

    @Column(name = "min_bedrooms", nullable = false)
    private Double minBedrooms;

    @Column(name = "max_bedrooms", nullable = false)
    private Double maxBedrooms;

    @Column(name = "min_bathrooms", nullable = false)
    private Integer minBathrooms;

    @Column(name = "max_bathrooms", nullable = false)
    private Integer maxBathrooms;

}
