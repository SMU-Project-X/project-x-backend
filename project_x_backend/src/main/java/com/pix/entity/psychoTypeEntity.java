package com.pix.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "psycho_type")
public class psychoTypeEntity {

    @Id
    @Column(name = "psycho_type_name", length = 100)
    private String psychoTypeName;

    @Column(name = "description", length = 255)
    private String description;
}
