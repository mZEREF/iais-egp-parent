package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class KPGameDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String categoryId;

    private String gameName;

    private String gameDescription;

    private Date issueDate;

    private BigDecimal price;

}
