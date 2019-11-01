package com.ecquaria.cloud.moh.iais.base;

/*
 *author: yichen
 *date time:9/26/2019 3:48 PM
 *description:
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseItem {
    private Integer id;
    private String rowguid;
    private Integer dsads;
    private String module;
    private String type;
    private String sectionId;
    private String regulationId;
    private String svcSubTypeId;
    private Integer itemId;
    private Integer order;
    private Integer parentId;
    private Integer dependId;
    private String dependValue;
    private boolean active;
}
