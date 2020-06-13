package com.nimesh.real_estate_ms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "property")
public class Property extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double price;

    @Column(name = "bedroom_count", nullable = false)
    private Double bedroomCount;

    @Column(name = "bathroomCount", nullable = false)
    private Integer bathroomCount;
}

