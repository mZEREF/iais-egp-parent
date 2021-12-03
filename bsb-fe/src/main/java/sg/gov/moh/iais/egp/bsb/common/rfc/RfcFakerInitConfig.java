package sg.gov.moh.iais.egp.bsb.common.rfc;

import com.google.common.collect.Maps;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;

import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.RfcFlowType.*;

/**
 * Faker initial rfc configuration
 * @author : LiRan
 * @date : 2021/11/9
 */
public class RfcFakerInitConfig {

    private RfcFakerInitConfig(){}

    public static Map<String, RfcFlowType> getFacRegFlowConfig(){
        Map<String,RfcFlowType> map = Maps.newHashMapWithExpectedSize(63);
        //facProfile config
        map.put("iais.bsbfe.facProfile.name",AMENDMENT);
        map.put("iais.bsbfe.facProfile.type",AMENDMENT);
        map.put("iais.bsbfe.facProfile.postalCode",AMENDMENT);
        map.put("iais.bsbfe.facProfile.blkNo",AMENDMENT);
        map.put("iais.bsbfe.facProfile.floorNo",AMENDMENT);
        map.put("iais.bsbfe.facProfile.unitNo",AMENDMENT);
        map.put("iais.bsbfe.facProfile.streetName",AMENDMENT);
        map.put("iais.bsbfe.facProfile.isProtected",AMENDMENT);
        //facOperator config
        map.put("iais.bsbfe.facOperator.name",AMENDMENT);
        map.put("iais.bsbfe.facOperator.idType",AMENDMENT);
        map.put("iais.bsbfe.facOperator.idNumber",AMENDMENT);

        map.put("iais.bsbfe.facOperator.designeeName", NOTIFICATION);
        map.put("iais.bsbfe.facOperator.nationality", NOTIFICATION);
        map.put("iais.bsbfe.facOperator.designation", NOTIFICATION);
        map.put("iais.bsbfe.facOperator.contactNo", NOTIFICATION);
        map.put("iais.bsbfe.facOperator.email", NOTIFICATION);
        map.put("iais.bsbfe.facOperator.employmentStartDate", NOTIFICATION);
        //facAuthoriser config
        map.put("iais.bsbfe.facAuthoriser.addOrDelete",AMENDMENT);
        map.put("iais.bsbfe.facAuthoriser.name",AMENDMENT);
        map.put("iais.bsbfe.facAuthoriser.idType",AMENDMENT);
        map.put("iais.bsbfe.facAuthoriser.idNumber",AMENDMENT);
        map.put("iais.bsbfe.facAuthoriser.securityClearanceDate", AMENDMENT);

        map.put("iais.bsbfe.facAuthoriser.nationality", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.designation", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.contactNo", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.email", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.employmentStartDate", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.employmentPeriod", NOTIFICATION);
        map.put("iais.bsbfe.facAuthoriser.workArea", NOTIFICATION);
        //facAdmin config
        map.put("iais.bsbfe.facAdmin.name",AMENDMENT);
        map.put("iais.bsbfe.facAdmin.idType",AMENDMENT);
        map.put("iais.bsbfe.facAdmin.idNumber",AMENDMENT);

        map.put("iais.bsbfe.facAdmin.nationality",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.designation",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.contactNo",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.email",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.employmentStartDate",NOTIFICATION);
        //facOfficer config
        map.put("iais.bsbfe.facOfficer.name",AMENDMENT);
        map.put("iais.bsbfe.facOfficer.idType",AMENDMENT);
        map.put("iais.bsbfe.facOfficer.idNumber",AMENDMENT);

        map.put("iais.bsbfe.facOfficer.nationality",NOTIFICATION);
        map.put("iais.bsbfe.facOfficer.designation",NOTIFICATION);
        map.put("iais.bsbfe.facOfficer.contactNo",NOTIFICATION);
        map.put("iais.bsbfe.facOfficer.email",NOTIFICATION);
        map.put("iais.bsbfe.facOfficer.employmentStartDate",NOTIFICATION);
        //facilityCommittee config
        map.put("iais.bsbfe.facilityCommittee.addOrDelete",AMENDMENT);
        map.put("iais.bsbfe.facilityCommittee.name",AMENDMENT);
        map.put("iais.bsbfe.facilityCommittee.idType",AMENDMENT);
        map.put("iais.bsbfe.facilityCommittee.idNumber",AMENDMENT);
        map.put("iais.bsbfe.facilityCommittee.role",AMENDMENT);

        map.put("iais.bsbfe.facilityCommittee.nationality",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.designation",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.contactNo",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.email",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.employmentStartDate",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.expertiseArea",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.employee",NOTIFICATION);
        map.put("iais.bsbfe.facilityCommittee.externalCompName",NOTIFICATION);
        //bat config
        map.put("iais.bsbfe.facBat.addOrDelete",AMENDMENT);
        map.put("iais.bsbfe.facBat.schedule",AMENDMENT);
        map.put("iais.bsbfe.facBat.name",AMENDMENT);
        map.put("iais.bsbfe.facBat.sample.addOrDelete",AMENDMENT);
        map.put("iais.bsbfe.facBat.sample.other",AMENDMENT);
        return map;
    }

    public static Map<String,RfcFlowType> getFacCerRegFlowConfig(){
        Map<String,RfcFlowType> map = Maps.newHashMapWithExpectedSize(43);
        //organisation config
        map.put("iais.bsbfe.organisation.name",AMENDMENT);
        map.put("iais.bsbfe.organisation.yearEstablished",AMENDMENT);

        map.put("iais.bsbfe.organisation.addressType",NOTIFICATION);
        map.put("iais.bsbfe.organisation.floor",NOTIFICATION);
        map.put("iais.bsbfe.organisation.unitNo",NOTIFICATION);
        map.put("iais.bsbfe.organisation.building",NOTIFICATION);
        map.put("iais.bsbfe.organisation.streetName",NOTIFICATION);
        map.put("iais.bsbfe.organisation.address1",NOTIFICATION);
        map.put("iais.bsbfe.organisation.address2",NOTIFICATION);
        map.put("iais.bsbfe.organisation.address3",NOTIFICATION);
        map.put("iais.bsbfe.organisation.postalCode",NOTIFICATION);
        map.put("iais.bsbfe.organisation.city",NOTIFICATION);
        map.put("iais.bsbfe.organisation.state",NOTIFICATION);
        map.put("iais.bsbfe.organisation.country",NOTIFICATION);
        map.put("iais.bsbfe.organisation.email",NOTIFICATION);
        map.put("iais.bsbfe.organisation.contactNo",NOTIFICATION);
        map.put("iais.bsbfe.organisation.contactPerson",NOTIFICATION);
        //admin config
        map.put("iais.bsbfe.facAdmin.name",AMENDMENT);
        map.put("iais.bsbfe.facAdmin.idType",AMENDMENT);
        map.put("iais.bsbfe.facAdmin.idNo",AMENDMENT);

        map.put("iais.bsbfe.facAdmin.nationality",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.designation",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.contactNo",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.email",NOTIFICATION);
        map.put("iais.bsbfe.facAdmin.employmentStartDate",NOTIFICATION);
        //CertifierTeam config
        map.put("iais.bsbfe.certifierTeamMember.addOrDelete",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.name",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.idType",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.idNo",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.birthDate",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.sex",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.leadCertifier",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.expertiseArea",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.certBSL3Exp",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.commBSL34Exp",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.otherBSL34Exp",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.eduBackground",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.proActivities",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.proRegAndCert",AMENDMENT);
        map.put("iais.bsbfe.certifierTeamMember.facRelatedPub",AMENDMENT);

        map.put("iais.bsbfe.certifierTeamMember.nationality",NOTIFICATION);
        map.put("iais.bsbfe.certifierTeamMember.telNo",NOTIFICATION);
        map.put("iais.bsbfe.certifierTeamMember.jobDesignation",NOTIFICATION);
        return map;
    }
}
