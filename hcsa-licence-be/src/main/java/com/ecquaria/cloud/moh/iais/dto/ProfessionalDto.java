package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;
import org.springframework.boot.actuate.metrics.http.Outcome;

import java.io.Serializable;

/**
 * @author Wenkang
 * @date 2020/9/16 9:31
 */
@Data
public class ProfessionalDto implements Serializable {
    private static final long serialVersionUID = -858384537130346730L;

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
