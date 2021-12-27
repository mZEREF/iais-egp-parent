package sg.gov.moh.iais.egp.bsb.dto.appview.afc;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrganisationProfileDto implements Serializable {
    private String facCertEntityId;
    private String orgName;
    private String addressType;
    private String floor;
    private String unitNo;
    private String building;
    private String streetName;
    private String address1;
    private String address2;
    private String address3;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String yearEstablished;
    private String email;
    private String contactNo;
    private String contactPerson;
}
