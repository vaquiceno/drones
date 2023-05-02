package com.drones.models.database;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table( name = "Medication")
public class Medication {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private Integer weight;
    @Column(name = "image_url")
    private String imageUrl;

}
