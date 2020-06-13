//package com.nimesh.real_estate_ms.entity;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import javax.persistence.*;
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Entity
//@Table(name="requirement_property_match")
//public class RequirementPropertyMatch extends BaseEntity {
//
//    @ManyToOne
//    @JoinColumn(name = "requirement_id")
//    private Requirement requirement;
//
//    @ManyToOne
//    @JoinColumn(name = "property_id")
//    private Property property;
//
//    @Column(name = "match_percentage")
//    private float matchPercentage;
//
//}
