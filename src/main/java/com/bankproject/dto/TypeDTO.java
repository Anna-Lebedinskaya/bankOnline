package com.bankproject.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TypeDTO extends GenericDTO{
    private String title;
    private String description;
}
