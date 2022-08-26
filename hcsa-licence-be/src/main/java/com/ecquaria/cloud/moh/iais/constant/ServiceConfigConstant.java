package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;

/**
 * ServiceConfigConstant
 *
 * @author suocheng
 * @date 7/11/2022
 */

public class ServiceConfigConstant {

    public static final String[] SERVICE_CODE ={HcsaConsts.SERVICE_TYPE_BASE,HcsaConsts.SERVICE_TYPE_OTHERS,HcsaConsts.SERVICE_TYPE_SPECIFIED};
    public static Map<String, Integer> SEQ_MAP = IaisCommonUtils.genNewHashMap();
    public static Map<String, String> NAME_MAP = IaisCommonUtils.genNewHashMap();
    public static Map<String, String> PREMISES_TYPE_MAP = Maps.newLinkedHashMap();
    //Service Doc personnel
    public static Map<String, String> SERVICE_DOC_PERSONNEL_BASE = IaisCommonUtils.genNewHashMap();
    public static Map<String, String> SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM = IaisCommonUtils.genNewHashMap();
    public static Map<String, String> SERVICE_DOC_PERSONNEL_SPECIAL = IaisCommonUtils.genNewHashMap();


    static {
        PREMISES_TYPE_MAP.put(ApplicationConsts.PREMISES_TYPE_PERMANENT,"Permanent Premises");
        PREMISES_TYPE_MAP.put(ApplicationConsts.PREMISES_TYPE_CONVEYANCE,"Conveyance");
        PREMISES_TYPE_MAP.put(ApplicationConsts.PREMISES_TYPE_MOBILE,"Mobile Delivery");
        PREMISES_TYPE_MAP.put(ApplicationConsts.PREMISES_TYPE_REMOTE,"Remote Delivery");

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
        SEQ_MAP.put(HcsaConsts.STEP_OTHER_INFORMATION, 13);
        SEQ_MAP.put(HcsaConsts.STEP_SUPPLEMENTARY_FORM, 14);
        SEQ_MAP.put(HcsaConsts.STEP_SPECIAL_SERVICES_FORM, 15);
        SEQ_MAP.put(HcsaConsts.STEP_DOCUMENTS, 16);
        SEQ_MAP.put(HcsaConsts.STEP_OUTSOURCED_PROVIDERS, 17);
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
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST);
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER, ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER);
        NAME_MAP.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES, ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_OPERATING_THEATRE_TRAINED_NURSE, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_OPERATING_THEATRE_TRAINED_NURSE);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_MEDICAL_SERVICE, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_MEDICAL_SERVICE);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_DENTAL_SERVICE, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_DENTAL_SERVICE);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTISING_DENTIST, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTISING_DENTIST);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ORAL_HEALTHCARE_THERAPIST, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ORAL_HEALTHCARE_THERAPIST);
        NAME_MAP.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTICING_DOCTOR, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTICING_DOCTOR);
        NAME_MAP = Collections.unmodifiableMap(NAME_MAP);

        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_OPERATING_THEATRE_TRAINED_NURSE,"Operating Theatre Trained Nurse");
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE, "Nurse in Charge");
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_MEDICAL_SERVICE,"Nurses (Medical Service)" );
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSES_DENTAL_SERVICE, "Nurses (Dental Service)");
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTISING_DENTIST, "Practicing Dentist");
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ORAL_HEALTHCARE_THERAPIST, "Oral Healthcare Therapist");
        SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTICING_DOCTOR, "Practicing Doctor");

        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "Clinical Governance Officer (CGO)");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, "Section Leader");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "Principal Officer (PO)");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "Nominee");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_KAH, "Key Appointment Holder (KAH)");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "Clinical Director");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, "Service Personnel");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_VEHICLES, "Vehicles");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_CHARGES, "General Conveyance Charges");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_CHARGES_OTHER, "Medical Equipment and Other Charges");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, "MedAlert Person");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, "Embryologist");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER, "AR Practitioner");
        SERVICE_DOC_PERSONNEL_BASE.put(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES, "Nurses");


        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "Clinical Governance Officer (CGO)");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, "Section Leader");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR, "Emergency Department Director");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR, "Emergency Department Nursing Director");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, "Nurse In Charge");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, "Radiation Safety Officer (RSO)");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR, "Diagnostic Radiographer");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, "Medical Physicist");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, "Radiation Physicist");
        SERVICE_DOC_PERSONNEL_SPECIAL.put(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM, "NM Technologist");


    }
}
