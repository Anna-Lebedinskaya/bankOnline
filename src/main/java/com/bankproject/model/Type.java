package com.bankproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "types")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "types_seq", allocationSize = 1)
public class Type extends GenericModel{
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
