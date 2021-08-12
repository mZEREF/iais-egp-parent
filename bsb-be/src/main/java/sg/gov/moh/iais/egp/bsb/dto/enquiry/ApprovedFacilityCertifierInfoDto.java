package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 15:59
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
public class ApprovedFacilityCertifierInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String organisationName;
    private String organisationAddress;
    private String AFCStatus;
    private String administrator;
    private Date approvedDate;
    private Date expiryDate;
    private String action;
}
