package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class PostCodeDto {
    @NotNull(message = "Postal Code is mandatory.")
    @NotBlank(message = "Postal Code is mandatory.")
    @Getter
    @Setter
    private String postalCode;
    @Getter
    @Setter
    private String addressType;
    @Getter
    @Setter
    private String blkHseNo;
    @Getter
    @Setter
    private String streetName;
    @Getter
    @Setter
    private String buildingName;
}
