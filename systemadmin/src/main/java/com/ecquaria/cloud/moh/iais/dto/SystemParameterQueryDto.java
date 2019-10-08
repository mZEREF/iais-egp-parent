package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/*
 *author: yichen
 *date time:9/4/2019 5:39 PM
 *description:
 */
@Getter
@Setter
public class SystemParameterQueryDto implements Serializable {
    private static final long serialVersionUID = 7811152537272121632L;

    private String id;

    @NotBlank(message = "can not is blank!", profiles = {"edit", "search"})
    @NotNull(message = "can not is null!", profiles = {"edit", "search"})
    private String domainType;

    private String module;

    private String description;

    private String valueType;

    private String value;

    private Character status;

    private String updatedBy;

    private Date updatedOn;
}
