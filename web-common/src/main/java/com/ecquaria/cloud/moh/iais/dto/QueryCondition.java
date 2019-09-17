package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/*
 *author: yichen
 *date time:9/5/2019 10:44 AM
 *description:
 */

@Getter
@Setter
@Component
public class QueryCondition {

    private int pageNo = 1;
    private int pageSize = 10;

    //for query dto
    private Class<? extends Serializable> clz;
    private String searchAttr;
    private String resultAttr;
    private String sortField;
}
