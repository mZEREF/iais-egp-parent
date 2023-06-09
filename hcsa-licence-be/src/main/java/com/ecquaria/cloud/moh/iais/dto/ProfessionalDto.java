package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Wenkang
 * @date 2020/9/16 9:31
 */
@Data
public class ProfessionalDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String IndentificationNo;

    private String CaseNo;

    private String CaseTypeDescription;

    private String CaseStatusDescription;

    private String OffenceDescription;

    private String OutcomeDescription;

    private String OutcomeIssueDate;

    private String ProsecutonOutcomeDescription;

    private String CreatedDate;

    private String UpdateDate;

}
