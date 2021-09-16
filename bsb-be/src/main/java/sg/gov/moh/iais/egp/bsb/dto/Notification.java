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

    private String facId;

    private String reqType;

    //Application
    private String applicant;

    //value(Main Admin/Alternate Admin)
    private String admin;

    //BISNEW001
    private String facilityClassification;

    //BISNEW002  value(Pre-Inspection/Pre-Certification)
    private String preAction;

    //BISNEW003,BISNEW004
    private String applicationType;

    private String status;

    //BISNEW005
    private String title;

    //+BISNEW006
    private Map<String,byte[]> attachments;

    private String additionalInfo;

    //Revocation

    //AO - BISEmail001
    private String approvalNo;

    //+BISNEW001,BISNEW008(tie with facilityClassification)
    private String facilityType;

    //+BISNEW001
    private String approvalType;

    //+BISNEW001
    private String facilityCertifier;

    private String officer;

    //USER - BISEmail002
    private String reason;

    //+BISNEW008
    private String facilityName;

    private String facilityAddress;

    //+BISNEW002\5\6
    private String date;

    //auto
    private Map<String,Object> contentParams;

    private Map<String,Object> subjectParams;
}
