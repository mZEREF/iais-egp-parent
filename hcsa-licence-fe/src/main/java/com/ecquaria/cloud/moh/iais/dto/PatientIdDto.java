package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PatientIdDto implements Serializable {
    private String idType;
    private String idNo;
    public PatientIdDto(){}
    public PatientIdDto(String idType, String idNo){
        this.idType = idType;
        this.idNo = idNo;
    }
}
