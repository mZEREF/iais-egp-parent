package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author YiMing
 * DATE:2021/9/10 9:12
 **/
@Data
public class Notification {
    //all
    private String applicationNo;

    //Application
    private String applicationName;

    private String applicationType;

    private String status;

    //Revocation

    //AO - rej001
    private String approvalNo;

    private String facilityType;

    private String approvalType;

    private String facilityCertifier;

    private String officer;

    //USER - rej002
    private String reason;

    private String facilityName;

    private String facilityAddress;

    private String date;

    //auto
    private Map<String,Object> contentParams;

    private Map<String,Object> subjectParams;
}
