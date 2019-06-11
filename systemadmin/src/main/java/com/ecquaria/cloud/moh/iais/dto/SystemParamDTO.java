package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Date;

public class SystemParamDTO {

    /*@NotNull(message = "id is mandatory.")
    @NotBlank(message = "id is mandatory.")*/
    @Size(min = 10,max = 255, message = "The length of id should be in 10 to 255")
   /* @ApiModelProperty(value="id", required=true)*/
    @Setter @Getter
    private String id;

    @Setter @Getter private String description;

    /*@NotNull(message = "value is mandatory.")
    @NotBlank(message = "value is mandatory.")
    @Size(min = 10,max = 255, message = "The length of value should be in 10 to 255")
    @ApiModelProperty(value="value", required=true)*/
    @Setter @Getter private String value;

    @Setter @Getter private String units;
    @Setter @Getter private Date createdAt;
    @Setter @Getter private Long createdBy;
    @Setter @Getter private Date modifiedAt;
    @Setter @Getter private Long modifiedBy;
    @Setter @Getter private String paramType;
    @Setter @Getter private Integer manDatory;
    @Setter @Getter private Integer canUpdate;
    @Setter @Getter private Integer maxLength;
    @Setter @Getter private Integer enable;
    @Setter @Getter private Integer enableCheckBox;
}
