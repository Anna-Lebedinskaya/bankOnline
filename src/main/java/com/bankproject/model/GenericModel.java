package com.bankproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass //в БД такой таблицы создаваться не будет, только в наследуемых классах
public abstract class GenericModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
    private Long id;

    @Column(name = "created_when")
    private LocalDateTime createdWhen;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "deleted_when")
    private LocalDateTime deletedWhen;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;
}
