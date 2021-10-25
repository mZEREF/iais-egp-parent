package sg.gov.moh.iais.egp.bsb.entity;



import lombok.Data;


import javax.persistence.*;
import java.util.Date;


@Data

public class FacilityOperator{
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
