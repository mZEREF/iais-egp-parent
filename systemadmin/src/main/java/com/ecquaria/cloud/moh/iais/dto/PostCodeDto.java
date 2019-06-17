package com.ecquaria.cloud.moh.iais.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class PostCodeDto {
    @NotNull(message = "Postal Code is mandatory.")
    @NotBlank(message = "Postal Code is mandatory.")
    @ApiModelProperty(value = "postalCode", required = true)
    @Getter
    @Setter
    private String postalCode;
    @ApiModelProperty(value = "addressType", required = false)
    @Getter
    @Setter
    private String addressType;
    @ApiModelProperty(value = "blkHseNo", required = false)
    @Getter
    @Setter
    private String blkHseNo;
    @ApiModelProperty(value = "streetName", required = false)
    @Getter
    @Setter
    private String streetName;
    @ApiModelProperty(value = "buildingName", required = false)
    @Getter
    @Setter
    private String buildingName;
}
