package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

@Data
public class PatientIdDto {
    private String idType;
    private String idNo;
    public PatientIdDto(){}
    public PatientIdDto(String idType, String idNo){
        this.idType = idType;
        this.idNo = idNo;
    }
}
