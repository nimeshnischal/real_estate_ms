package com.nimesh.real_estate_ms.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
class BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
