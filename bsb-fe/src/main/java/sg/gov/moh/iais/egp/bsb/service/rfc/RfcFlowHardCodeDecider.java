package sg.gov.moh.iais.egp.bsb.service.rfc;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.RfcFlowType.AMENDMENT;
import static sg.gov.moh.iais.egp.bsb.constant.RfcFlowType.DO_NOTHING;
import static sg.gov.moh.iais.egp.bsb.constant.RfcFlowType.NOTIFICATION;


@Slf4j
@Component
public class RfcFlowHardCodeDecider implements RfcFlowDecider {
    private static final Map<String, RfcFlowType> FACILITY_RFC_DECISION_MAP;
    public static final Set<String> FACILITY_RFC_SCREEN_FIELD_SET;
    public static final Map<String, RfcFlowType> APPROVAL_RFC_DECISION_MAP;
    public static final Set<String> APPROVAL_RFC_EDIT_FIELD_SET;

    static {
        Map<String, RfcFlowType> facilityRfcDecisionMap = Maps.newHashMapWithExpectedSize(63);

        //facProfile config
        facilityRfcDecisionMap.put("facProfile.facName", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.facType", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.sameAddressAsCompany", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.postalCode", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.blkNo", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.floorNo", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.unitNo", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.streetName", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.building", AMENDMENT);
        facilityRfcDecisionMap.put("facProfile.isProtected", AMENDMENT);
        //facOperator config
        facilityRfcDecisionMap.put("facOperator.facOperator", AMENDMENT);
        facilityRfcDecisionMap.put("facOperator.salutation", NOTIFICATION);
        facilityRfcDecisionMap.put("facOperator.designeeName", AMENDMENT);
        facilityRfcDecisionMap.put("facOperator.idType", AMENDMENT);
        facilityRfcDecisionMap.put("facOperator.idNumber", AMENDMENT);
        facilityRfcDecisionMap.put("facOperator.nationality", NOTIFICATION);
        facilityRfcDecisionMap.put("facOperator.designation", NOTIFICATION);
        facilityRfcDecisionMap.put("facOperator.contactNo", NOTIFICATION);
        facilityRfcDecisionMap.put("facOperator.email", NOTIFICATION);
        facilityRfcDecisionMap.put("facOperator.employmentStartDate", NOTIFICATION);
        //facAdmin config
        facilityRfcDecisionMap.put("facAdmin.salutation", NOTIFICATION);
        facilityRfcDecisionMap.put("facAdmin.name", AMENDMENT);
        facilityRfcDecisionMap.put("facAdmin.idType", AMENDMENT);
        facilityRfcDecisionMap.put("facAdmin.idNumber", AMENDMENT);
        facilityRfcDecisionMap.put("facAdmin.nationality", NOTIFICATION);
        facilityRfcDecisionMap.put("facAdmin.designation", NOTIFICATION);
        facilityRfcDecisionMap.put("facAdmin.contactNo", NOTIFICATION);
        facilityRfcDecisionMap.put("facAdmin.email", NOTIFICATION);
        facilityRfcDecisionMap.put("facAdmin.employmentStartDate", NOTIFICATION);
        //facOfficer config
        facilityRfcDecisionMap.put("facOfficer.salutation", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.name", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.idType", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.idNumber", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.nationality", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.designation", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.contactNo", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.email", NOTIFICATION);
        facilityRfcDecisionMap.put("facOfficer.employmentStartDate", NOTIFICATION);
        //facAuthoriser config
        facilityRfcDecisionMap.put("facAuthoriser", AMENDMENT);
        //facilityCommittee config
        facilityRfcDecisionMap.put("facilityCommittee", AMENDMENT);
        //supporting doc config
        facilityRfcDecisionMap.put("facSupportingDoc", AMENDMENT);

        FACILITY_RFC_DECISION_MAP = Collections.unmodifiableMap(facilityRfcDecisionMap);

        FACILITY_RFC_SCREEN_FIELD_SET = new HashSet<>(Arrays.asList(
                "facProfile.sameAddressAsCompany",
                "facProfile.postalCode",
                "facProfile.blkNo",
                "facProfile.floorNo",
                "facProfile.unitNo",
                "facProfile.streetName",
                "facProfile.building"
        ));


        Map<String, RfcFlowType> approvalRfcDecisionMap = Maps.newHashMapWithExpectedSize(4);
        // facility authoriser config
        approvalRfcDecisionMap.put("authorisedPersonnel.involvedWork", AMENDMENT);
        approvalRfcDecisionMap.put("authorisedPersonnel.isPerformed", AMENDMENT);
        /** Common **/
        approvalRfcDecisionMap.put("bat.others",AMENDMENT);
        approvalRfcDecisionMap.put("approvalSupportingDoc", AMENDMENT);

        APPROVAL_RFC_DECISION_MAP = Collections.unmodifiableMap(approvalRfcDecisionMap);


        Set<String> approvalRfcEditFields = new HashSet<>(4);
        // bat config
        /** Approval to Possess **/
        approvalRfcEditFields.add("sampleType");
        approvalRfcEditFields.add("workType");
        approvalRfcEditFields.add("sampleWorkDetail");
        approvalRfcEditFields.add("procurementMode");
        /** Approval to Large Scale Produce **/
        approvalRfcEditFields.add("estimatedMaximumVolume");
        approvalRfcEditFields.add("methodOrSystem");
        /** Special Approval to Handle **/
        approvalRfcEditFields.add("projectName");
        approvalRfcEditFields.add("principalInvestigatorName");
        approvalRfcEditFields.add("intendedWorkActivity");
        approvalRfcEditFields.add("activityStartDt");
        approvalRfcEditFields.add("activityEndDt");
        approvalRfcEditFields.add("activityRemarks");
        approvalRfcEditFields.add("involvedWork");
        approvalRfcEditFields.add("isPerformed");
        // Transferring Facility config
        approvalRfcEditFields.add("facNameT");
        approvalRfcEditFields.add("postalCodeT");
        approvalRfcEditFields.add("addressTypeT");
        approvalRfcEditFields.add("blockNoT");
        approvalRfcEditFields.add("floorNoT");
        approvalRfcEditFields.add("unitNoT");
        approvalRfcEditFields.add("streetNameT");
        approvalRfcEditFields.add("buildingNameT");
        // Contact Person from Transferring Facility config
        approvalRfcEditFields.add("contactPersonNameT");
        approvalRfcEditFields.add("emailAddressT");
        approvalRfcEditFields.add("contactNoT");
        approvalRfcEditFields.add("expectedDateT");
        approvalRfcEditFields.add("courierServiceProviderNameT");
        approvalRfcEditFields.add("remarksT");
        // Exporting Facility config
        approvalRfcEditFields.add("facNameE");
        approvalRfcEditFields.add("postalCodeE");
        approvalRfcEditFields.add("addressTypeE");
        approvalRfcEditFields.add("blockNoE");
        approvalRfcEditFields.add("floorNoE");
        approvalRfcEditFields.add("unitNoE");
        approvalRfcEditFields.add("streetNameE");
        approvalRfcEditFields.add("buildingNameE");
        approvalRfcEditFields.add("countryE");
        approvalRfcEditFields.add("cityE");
        approvalRfcEditFields.add("stateE");
        // Contact Person from Exporting Facility config
        approvalRfcEditFields.add("contactPersonNameE");
        approvalRfcEditFields.add("emailAddressE");
        approvalRfcEditFields.add("contactNoE");
        approvalRfcEditFields.add("expectedDateE");
        approvalRfcEditFields.add("courierServiceProviderNameE");
        approvalRfcEditFields.add("remarksE");
        // supporting doc config
        approvalRfcEditFields.add("approvalSupportingDoc");

        APPROVAL_RFC_EDIT_FIELD_SET = Collections.unmodifiableSet(approvalRfcEditFields);
    }


    @Override
    public RfcFlowType decide4Facility(Collection<String> changedKeys) {
        RfcFlowType flowType = DO_NOTHING;
        if (changedKeys != null && !changedKeys.isEmpty()) {
            for (String key : changedKeys) {
                RfcFlowType typeForKey = FACILITY_RFC_DECISION_MAP.getOrDefault(key, DO_NOTHING);
                if ((typeForKey.ordinal() - flowType.ordinal()) > 0) {
                    flowType = typeForKey;
                }
            }
        }
        return flowType;
    }

    @Override
    public RfcFlowType decide4Approval(Collection<String> changedKeys) {
        RfcFlowType flowType = DO_NOTHING;
        if (changedKeys != null && !changedKeys.isEmpty()) {
            for (String key : changedKeys) {
                RfcFlowType typeForKey = APPROVAL_RFC_DECISION_MAP.getOrDefault(key, DO_NOTHING);
                if ((typeForKey.ordinal() - flowType.ordinal()) > 0) {
                    flowType = typeForKey;
                }
            }
        }
        return flowType;
    }
}
