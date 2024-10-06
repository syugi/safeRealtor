package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyResDTO {
    private PropertyDTO property;
    private boolean imageUploadSuccess;
    private String message;
}
