package sg.gov.moh.iais.egp.bsb.entity;



import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.persistence.*;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
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
