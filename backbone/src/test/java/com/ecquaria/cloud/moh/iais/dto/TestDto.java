package com.ecquaria.cloud.moh.iais.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class TestDto {
    @NotNull(message = "operation is mandatory")
    @ApiModelProperty(value = "operation", required = true)
    @Getter
    @Setter
    private String operation;
}
