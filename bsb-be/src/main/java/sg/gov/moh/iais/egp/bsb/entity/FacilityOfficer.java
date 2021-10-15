package sg.gov.moh.iais.egp.bsb.entity;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;


@Data
public class FacilityOfficer extends BaseEntity {

    private String id;

    private Facility facility;

    private String name;

    private String nationality;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String email;

    private Date employmentStartDate;
}
