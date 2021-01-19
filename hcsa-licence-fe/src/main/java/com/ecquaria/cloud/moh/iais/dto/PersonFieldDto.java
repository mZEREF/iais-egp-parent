package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ZiXian
 */
@Getter
@Setter
public class PersonFieldDto implements Serializable {
    private static final long serialVersionUID = -5327342580937859116L;

    private String name;
    private String salutation;
    private String idType;
    private String  idNo;
    private String designation;
    private String mobileNo;
    private String officeTelNo;
    private String emailAddr;
    private String professionType;
    private String profRegNo;
    private String speciality;
    private String specialityOther;
    private String subSpeciality;
    private String preferredMode;
    //sp
    private String wrkExpYear;
    private String qualification;

}
