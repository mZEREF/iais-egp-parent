package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.web.logging.dto.AuditTrailDto;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

/**
 * AppGrpPremisesDto
 *
 * @author suocheng
 * @date 9/26/2019
 */

@Getter @Setter
public class AppGrpPremisesDto implements Serializable {
    private static final long serialVersionUID = 2152082123293571140L;

    private Integer id;

    private String rowguid;
    // do not user in the page
    private String hciCode;

    private String premisesSelect;

    //======================
    //for the On-site
    //======================
    //Name of premises
    private String hciName;
    //What is your premises type?
    @NotNull(message = "can not is null!", profiles = {"create", "edit"})
    @NotBlank(message = "can not is blank!", profiles = {"create", "edit"})
    private String premisesType;
     //Postal Code
     @NotNull(message = "can not is null!", profiles = {"create", "edit"})
     @NotBlank(message = "can not is blank!", profiles = {"create", "edit"})
    private String postalCode;

    //Block / House No.
    private String blkNo;
     //Street Name
     @NotNull(message = "can not is null!", profiles = {"create", "edit"})
     @NotBlank(message = "can not is blank!", profiles = {"create", "edit"})
    private String streetName;
      //Floor No.
    private String floorNo;
     //Unit No.
    private String unitNo;
     //Building Name
    private String buildingName;
     //Address Type
    private String addrType;

    private String siteSafefyNo;

    //======================
    //for the Conveyance
    //======================

    //Vehicle No.
    private String conveyanceVehicleNo;
    private String conveyanceSalutation;
    private String conveyanceVehicleOwnerName;
    private String conveyancePostalCode;
    private String conveyanceBlockNo;
    private String conveyanceStreetName;
    private String conveyanceFloorNo;
    private String conveyanceUnitNo;
    private String conveyanceBuildingName;
    private String conveyanceAddressType;
    private String conveyanceEmail;
    private String conveyanceMobile;

    private AuditTrailDto auditTrailDto;


}
