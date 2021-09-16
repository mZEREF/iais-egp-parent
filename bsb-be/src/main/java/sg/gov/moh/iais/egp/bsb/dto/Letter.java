package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.Map;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/10 17:46
 * DESCRIPTION: TODO
 **/

@Data
public class Letter {

    //all
    private  String applicant;

    private String facId;

    private String reqType;

    //1 ,6,8,9
    private  String designation;

    //1
    private  String companyName;

    //all
    private String emailAddress;

    //1,3,4,5
    private String agent;

    //all
    private String submissionDate;

    //1
    private String laboratoryName;

    //2,3,4,5,7
    private String operatorName;

    //all
    private String applicationNo;

    //2,7
    private String facilityName;

    //all
    private String nowDate;

    //2,3,4,5,6,7,9,10
    private String registrationNo;

    //ex 1,2
    private String specialist;

    //6,10
    private String expiryDate;

    private String letterType;

    private Map<String,Object> contentParams;

    private Map<String,Object> subjectParams;
}
