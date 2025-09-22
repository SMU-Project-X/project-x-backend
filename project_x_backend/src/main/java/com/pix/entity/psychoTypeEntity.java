package com.pix.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "psycho_type")
@Getter
@Setter
@ToString
public class psychoTypeEntity {

    @Id
    @Column(name = "psycho_type_name", length = 100)
    private String psychoTypeName;

    @Column(name = "description", length = 255)
    private String description;
}
