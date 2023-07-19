package com.bankproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // содержит геттеры, сеттеры, ToString, AllArgsConstructor
@NoArgsConstructor
public abstract class GenericDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String createdBy;
    private LocalDateTime deletedWhen;
    private String deletedBy;
    private boolean isDeleted;
}
