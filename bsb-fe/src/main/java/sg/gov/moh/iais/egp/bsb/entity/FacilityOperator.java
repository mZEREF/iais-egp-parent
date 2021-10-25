package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
public class FacilityOperator extends BaseEntity {
    private String id;

    private Facility facility;

    private String facOperator;

    private String designeeName;

    private String nationality;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String email;

    private Date employmentStartDate;
}
