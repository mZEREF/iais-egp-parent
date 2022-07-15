package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ServiceConfigConstant
 *
 * @author suocheng
 * @date 7/11/2022
 */

public class ServiceConfigConstant {

    public static final String[] SERVICE_CODE ={HcsaConsts.SERVICE_TYPE_BASE,HcsaConsts.SERVICE_TYPE_OTHERS,HcsaConsts.SERVICE_TYPE_SPECIFIED};
    public static Map<String, Integer> SEQ_MAP = new HashMap<>();
    public static Map<String, String> NAME_MAP = new HashMap<>();

    static {
        SEQ_MAP.put(HcsaConsts.STEP_BUSINESS_NAME, 1);
        SEQ_MAP.put(HcsaConsts.STEP_VEHICLES, 2);
        SEQ_MAP.put(HcsaConsts.STEP_CLINICAL_DIRECTOR, 3);
        SEQ_MAP.put(HcsaConsts.STEP_LABORATORY_DISCIPLINES, 4);
        SEQ_MAP.put(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, 5);
        SEQ_MAP.put(HcsaConsts.STEP_SECTION_LEADER, 6);
        SEQ_MAP.put(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, 7);
        SEQ_MAP.put(HcsaConsts.STEP_CHARGES, 8);
        SEQ_MAP.put(HcsaConsts.STEP_SERVICE_PERSONNEL, 9);
        SEQ_MAP.put(HcsaConsts.STEP_PRINCIPAL_OFFICERS, 10);
        SEQ_MAP.put(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER, 11);
        SEQ_MAP.put(HcsaConsts.STEP_MEDALERT_PERSON, 12);
        SEQ_MAP.put(HcsaConsts.STEP_DOCUMENTS, 13);
        SEQ_MAP = Collections.unmodifiableMap(SEQ_MAP);

        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "principalOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "DeputyPrincipalOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "ClinicalGovernanceOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, "ServicePersonnel");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, "MedalertPerson");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "clinical_director");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_VEHICLES, "vehicles");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES, "charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES_OTHER, "other-charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, "SectionLeader");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_KAH, "KAH");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, "RadiationPhysicist");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, "MedicalPhysicist");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, "RadiationSafetyOfficer");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, "NurseInCharge");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM, "NMTechnologist");
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR, "DiagnosticRadiographer");

        NAME_MAP = Collections.unmodifiableMap(NAME_MAP);
    }
}
