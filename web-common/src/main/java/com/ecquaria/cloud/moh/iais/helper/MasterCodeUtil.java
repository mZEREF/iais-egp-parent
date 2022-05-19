/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.helper.MasterCodeHelper;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * MasterCodeUtil
 *
 * @author Jinhua
 * @date 2019/7/25 16:20
 */
@Slf4j
public final class MasterCodeUtil {
    //Cache names

    private static final String SEQUENCE                           = "sequence";
    private static final String WEBCOMMON                          = "webcommon";
    private static final String RETRIEVE_MASTER_CODES              = "retrieveMasterCodes";

    //Code Categorys
    public static final String CATE_ID_RISK_LEVEL                  = "2CFD766C-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SERVICE_TYPE                = "0D675C87-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_APP_TYPE                    = "623F4561-1D0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_INSPEC_REQUIRED_TYPE        = "49E66DB0-1F0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_PRE_OR_POST_INSPEC          = "695F9F2B-200C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_RISK_RATING                 = "E9BAC83C-210C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_INS_OR_RRA_SOURCE           = "121589F8-210C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_LEAD_AND_GOVE_TAG           = "C1DD8781-220C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_APP_STATUS                  = "BEE661EE-220C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ADDRESS_TYPE                = "19DD1192-080C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ANSWER_TYPE                 = "35AA431C-270C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_COMMON_STATUS               = "8CEB1F80-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SALUTATION                  = "DFF3D597-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ID_TYPE                     = "73B14DAE-A87F-EA11-BE82-000C29F371DC";
    public static final String CATE_ID_RISK_WEIGHTAGE_MATRIX       = "199B036A-230C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DATE_TYPE                   = "1B1C8815-280C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DATE_RANGE                  = "38E90928-240C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_PAYMENT_STATUS              = "E2194D1C-0A0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_CHECKLIST_TYPE              = "47DD36ED-280C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_CHECKLIST_MODULE            = "9276F7F8-290C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SUB_TYPE_TYPE               = "E214989D-CF0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_MSG_TEMPLATE_TYPE           = "427AA14F-4A13-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_MSG_TEMPLATE_PROCESS        = "1E0ACD78-7134-EB11-8B7B-000C293F0C99";
    public static final String CATE_ID_DELIVERY_MODE               = "754889D0-5213-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DESIGNATION                 = "136A214B-8611-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROFESSIONAL_TYPE           = "444ADC42-8A11-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_RECOMMENDATION_TYPE         = "35829FFC-ED17-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROCESSING_DECISION         = "B8A4E683-F517-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_INSPECTION_STATUS           = "D4EA93FF-511A-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_SERVICE_STEP                = "E498C55D-9723-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SERVICE_PERSONNEL_PSN_TYPE  = "D44B5FF6-5028-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROCESS_FILE_TRACK_STATUS   = "38902DD7-1E29-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_APPLICATION_DRAFT_VALIDITY  = "A89CA599-179F-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_LICENCE_STATUS              = "CF69FBD0-B37A-4DD9-9BD6-0A153ED55BF8";
    public static final String CATE_ID_EFO_REASON                  = "4AADAE2F-A8D4-4966-A17B-BFBAE669E8E4";
    public static final String CATE_ID_DONATED_TYPE                = "FB120D85-0A85-4AEC-BF79-FB5598582345";
    public static final String CATE_ID_DONATION_REASON             = "FB121B45-0A85-4AEC-BF79-FB5598582345";
    public static final String CATE_ID_DISPOSAL_TYPE               = "FB133D85-0A85-4AEC-BF79-FB5598582345";
    public static final String CATE_ID_AGE_OF_EMBRYO_TRANSFER      = "5DAF8193-CBA8-414F-8873-5A3CC4794133";
    public static final String CATE_ID_ORDER_IN_ULTRASOUND         = "CDD1E8F5-92B9-4B7C-B66F-871A59703EB3";
    public static final String CATE_ID_OUTCOME_OFPREGNANCY         = "E54B1A96-9AE8-4D3C-B620-D8C6F60090DC";
    public static final String CATE_ID_MODE_OF_DELIVERY            = "BA3BDE36-50B7-442F-8A26-C4DD4A50A91B";
    public static final String CATE_ID_BABY_BIRTH_WEIGHT           = "6A3AD422-C5B8-44BB-A62F-A69CF558C006";
    public static final String CATE_ID_POS_BIRTH_PLACE             = "1AFFD970-9BEA-4E9D-97C7-CDA2CC4EC2B5";
    public static final String CATE_ID_POS_BABY_DEFECT             = "9FE7AEAE-C970-4177-B708-F72AE6899483";
    public static final String CATE_ID_ATS_ART_CO_FUNDING             = "8ED4B2DA-F459-4AF1-8012-97890C975121";

    public static final String CATE_ID_SYSTEM_PARAMETER_MODULE          = "1D3F6F1A-5334-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SYSTEM_PARAMETER_TYPE            = "364C7AFB-5234-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SYSTEM_PARAMETER_TYPE_OF_VALUE   = "D68BA451-5334-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_TEMPLATE_ROLE                    = "2C594569-0FB9-EA11-BE84-000C29F371DC";

    public static final String CATE_ID_SYSTEM_RISK_TYPE                 = "110534B1-4967-EA11-BE7F-000C29F371DC";
    public static final String CATE_ID_INBOX_MESSAGE_TYPE               = "D45AC717-945A-EA11-BE79-000C29D29DB0";
    public static final String CATE_ID_PERSONNEL_PSN_TYPE               = "6BC8C0B4-B182-EA11-BE82-000C29F371DC";
    public static final String CATE_ID_ERR_MSG_MODULE                   = "87F2F7C5-ABC5-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_ERR_MSG_TYPE                     = "FA7B1B4F-ADC5-EA11-BE84-000C29F371DC";
    public static final String CATE_ID_WRONG_TYPE                       = "02FC2CBE-ADC5-EA11-BE84-000C29F371DC";
    public static final String CATE_ID_PUBLIC_HOLIDAY                   = "21ADE137-CE1D-EB11-8B7A-000C293F0C99";
    public static final String CATE_ID_AUDIT_TRAIL_OPERATION_TYPE       = "6BB07B6A-F168-EA11-BE82-000C29F371DC";
    public static final String CATE_ID_MOH_RELATED_URLS                 = "B61C07F8-ACED-EA11-8B79-000C293F0C99";
    public static final String CATE_ID_DAY_NAMES                        = "D50DAB67-347A-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_PROFESSION_BOARD                 = "ED29FF07-7FA1-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_EAS_MTS_SPECIALTY                = "6DC1ED79-08A4-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_BANK_NAME                        = "137A214B-8611-EA11-BE78-874C29D29DB0";
    public static final String CATE_ID_CESSION_REASION                  = "20941117-A009-4E45-AD2F-22F233EAA28D";

    //charges
    public static final String CATE_ID_GENERAL_CONVEYANCE_CHARGES_TYPE                  = "D4F72703-47A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_MEDICAL_EQUIPMENT_AND_OTHER_CHARGES_CATEGORY     = "E6C7DED1-D5A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_AIRWAY_AND_VENTILATION_EQUIPMENT_CHARGES_TYPE    = "1E1A2D13-DAA3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_INTRAVENOUS_EQUIPMENT_CHARGES_TYPE               = "89706AEC-DBA3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_CARDIAC_EQUIPMENT_CHARGES_TYPE                   = "1F7D8F70-DCA3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_IMMOBILISATION_DEVICE_CHARGES_TYPE               = "24777D7F-DDA3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_TRAUMA_SUPPLIES_OR_EQUIPMENT_CHARGES_TYPE        = "1AFA016A-F4A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_INFECTION_CONTROL_EQUIPMENT_CHARGES_TYPE         = "BC3A0435-F5A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_MEDICATIONS_CHARGES_TYPE                         = "A721D57E-F5A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_MISCELLANEOUS_CHARGES_TYPE                       = "5ADA2A9A-F6A3-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_INSPECTION_ENTITY_TYPE                           = "796E6112-36B1-EB11-8B7D-000C293F0C99";
    public static final String CATE_ID_LICENSEE_TYPE                                    = "1CC74A81-ACA4-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_LICENSEE_SUB_TYPE                                = "AB50CD91-80D5-EB11-8B7F-000C29FD17F9";
    public static final String CATE_ID_FREE_RENEW_HCI_CODES                             = "C8D460B4-A20F-EC11-8B7E-000C293F0C99";

    public static final String DESIGNATION_OTHER_CODE_KEY                               = "DES999";

    //ApplicationInfo
    public static final String CATE_ID_BSB_APP_TYPE = "FF506BE0-75EF-EB11-8B7D-000C293F0C99";
    public static final String CATE_ID_BSB_FAC_TYPE = "5013CFE0-9A2B-4D28-ADFC-313383A3FDA8";
    public static final String CATE_ID_BSB_PRO_TYPE = "B538B60A-DFD1-4D1B-A668-8E9E4D61EC2A";
    public static final String CATE_ID_BSB_FAC_CLASSIFICATION = "856CC1D9-0272-41AA-AE64-C1C9815FA63E";
    public static final String CATE_ID_BSB_SCH_TYPE = "D0B94AC3-1FA4-4087-AFCC-C82622A77B80";
    public static final String CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT = "2C41F2F7-6D09-44AC-80B8-F1FA7BC8D38A";
    public static final String CATE_ID_BSB_APP_STATUS = "B37CA772-53BC-4CD5-A644-F5A34513B6C0";

    //FacilityInfo
    public static final String CATE_ID_BSB_GAZETTED_AREA = "5B9B1838-C58C-49E9-AA65-3078F67B1ED9";

    // Data Submission Id Type
    public static final String CATE_ID_DS_ID_TYPE = "AA1FF089-5B35-EC11-BE73-000C298D317C";

    // Data Submission Id Type
    public static final String CATE_ID_DS_STAGE_TYPE = "C51566B8-4C32-EC11-BE72-000C298D317C";

    // Data Submission Donor Identity Known
    public static final String CATE_DIK = "E8118F2B-5843-EC11-8B83-000C29FD17F9";

    // Nationality
    public static final String CATE_ID_NATIONALITY = "21941117-A009-4E45-AD2F-22F233EAA28D";

    // Ethnic Group
    public static final String CATE_ID_ETHNIC_GROUP = "22941117-A009-4E45-AD2F-22F233EAA28D";

    //Data Submission Current AR Treatment
    public static final String CURRENT_AR_TREATMENT = "27383D78-59F2-4AE1-878A-A65596D3DF1E";

    //Data Submission Source Of Semen
    public static final String SOURCE_OF_SEMEN = "ECC6C3F0-1E3E-4F1E-91A9-7EACA6DC7CC3";

    //Data Submission Ar Techniques Used
    public static final String AR_TECHNIQUES_USED = "50371FBA-6377-453F-B1CD-0EC89C6EA213";

    //Data Submission Ar Techniques Used
    public static final String AR_MAIN_INDICATION  = "BCCA3319-3881-464B-AE69-EA4C1FA95B2E";

    //Data Submission Ar Techniques Used
    public static final String AR_OTHER_INDICATION = "925F6CBB-A12D-4D15-9C85-6E4F09F8C44B";

    //Data Submission Ar donors used TYpe
    public static final String AR_DONOR_USED_TYPE = "C64D732A-9FA3-4D8E-AF7B-9B95B1645430";

    //Data Submission Ar Freezing Cryopreserved Options
    public static final String AR_FREE_CRYO_OPTIONS = "B456C51B-B13A-EC11-BE73-000C298D317C";

    //Data Submission Ar IUI Treatment Subsidies
    public static final String PLEASE_INDICATE_IUI_CO_FUNDING = "4AAB50BC-7F5B-4187-930C-A296491B3ACE";

    //Data Submission Outcome of Embryo Transferred
    public static final String OUTCOME_OF_EMBRYO_TRANSFERRED = "2D84C8E8-8CFA-46AD-8A00-4C01510A5D7F";

    //Data Submission Transfer In And Out
    public static final String WHAT_WAS_TRANSFERRED = "611D5523-86AA-4018-A095-93B01D1F2A29";
    public static final String TRANSFERRED_IN_FROM = "F8AB83C2-5C6A-4480-A253-DF757BB9C8F6";

    //Donor Sample Type
    public static final String AR_DONOR_SAMPLE_TYPE = "23F8DD6B-8843-EC11-BE6B-000C29FAAE4D";

    //Data Submission Ar End Cycle Stage
    public static final String END_CYCLE_REASON_FOR_ABANDONMENT = "5CA671D7-1D42-412B-AABA-1F3D0919379B";

    //Data Submission Type
    public static final String DATA_SUBMISSION_TYPE           = "6E7A2098-3C1C-4292-89E5-71C345A98028";
    //Data Submission Status
    public static final String DATA_SUBMISSION_STATUS          = "55fdea54-f29d-4a33-a7e2-184c4cdb8581";

    //Reason for Patient Amendment
    public static final String DATA_SUBMISSION_PATIENT_AMENDMENT = "E92D33E5-6B47-EC11-BE6B-000C29FAAE4D";
    public static final String DATA_SUBMISSION_DONOR_SMAPLE_AMENDMENT = "54C91975-375E-EC11-BE6B-000C29FAAE4D";

    public static final String DATA_SUBMISSION_CYCLE_STAGE_AMENDMENT = "688C8387-3F84-4C45-AAA9-8F2491BC1DBD";
    //Dp Drug Prescribed or Dispensed
    public static final String DP_DRUG_PRESCRIBED_OR_DISPENSED      ="E3B7DD31-CA12-4B18-B434-0B30BAA60E55";
    public static final String DP_MEDICATION                        ="8B29293A-DDF6-460A-AE10-731196AFBB47";
    public static final String DP_FREQUENCY                         ="2C288EBF-12D5-4D5F-9A1A-EAC6EEBDE127";

    public static final String DP_URINETESTTYPE                     ="11864921-52D6-EC11-BE6C-000C29FAAE4D";
    public static final String DP_URINETESTRESULT                   ="2419FC5C-52D6-EC11-BE6C-000C29FAAE4D";

    public static final  String DONOR_RELATION_TO_PATIENT           ="9C2B3AFF-55A8-445B-A759-0B806FEB3317";

    //Voluntary Sterilization Overview
    public static final String VSS_RESIDENCE_STATUS                 ="FFE0EB7A-86AE-4B62-85B1-8493D6EEF9B3";
    public static final String VSS_MARITAL_STATUS                   ="6B61A48F-BF8B-48AD-8FAD-20DBAD439ED1";
    public static final String VSS_EDUCATION_LEVEL                  ="7A9A6EC2-96FE-4AF9-858F-6F6830B018D1";
    public static final String VSS_STERILIZATION_REASON             ="A2725934-1C1D-489F-87FA-04192B2BB357";
    public static final String VSS_METHOD_OF_STERILIZATION          ="A7B66B93-3D27-4033-849C-3B059CA06A20";
    public static final String VSS_ETHNIC_GROUP                     ="7E43824F-C007-4325-B5D7-5709F79CDF88";
    public static final String VSS_OCCUPATION                       ="7A6E622A-F986-45E2-AD3F-13C3B5639702";
    //Termination of Pregnancy
    //Patient Information
    public static final String TOP_EDUCATION_LEVEL                       ="B0CDD9CD-8716-4867-89D0-00F31B86FD17";
    public static final String TOP_RESIDENCE_STATUS                      ="61EBD041-A0A5-42E6-9E8D-58506EC47008";
    public static final String TOP_MARITAL_STATUS                        ="2E43DE70-7859-4573-8BE6-5E79D5FF662E";
    public static final String TOP_ACTIVITY_STATUS                       ="D085416D-2B29-424C-A6BE-98B9821CC763";
    public static final String TOP_OCCUPATION                            ="D6652541-CC9F-4BB1-AE31-702A11788BD3";
    //Family Planning/Pregnancy
    public static final String TOP_CONTRACEPTIVE_HISTORY                 ="4D2AED4E-65BB-4218-8E14-878EC1723615";
    public static final String TOP_CONTRACEPTIVE_METHODS                 ="DDD11C62-E4BA-418C-A433-51E0EC20E94C";
    public static final String TOP_GENDER_OF_PREGNANT_CHILDREN           ="B1E7EFE9-9E58-4D06-A0FF-952DAFC9C278";
    public static final String TOP_GENDER_OF_PREGNANT_CHILDREN_UN        ="56124D4F-5F78-4861-A31A-6B9E496EB2F7";
    public static final String TOP_REASONS_TERMINATION_PREGNANCY         ="F48ADDE4-D209-4D96-9978-BB44F368DD00";
    public static final String TOP_SUB_CAUSES_TERMINATION_PREGNANCY      ="4972915B-0088-4A9E-BFC9-4BBCBFBD2FFC";
    //Pre-Termination Of Pregnancy Counselling
    public static final String TOP_CONSULTATION_RESULTS                  ="10BB38F8-5FBE-445F-B3DB-0C1BAA25472D";
    public static final String TOP_PRE_COUNSELLING_PLACE                 ="8129555A-974F-4076-9A46-1E01FAFBD066";
    public static final String TOP_FINAL_PRE_COUNSELLING_RESULT          ="408F78E6-FA22-4DAE-A43A-E1112063395E";
    //Termination Of Pregnancy
    public static final String TOP_TYPE_TERMINATION_PREGNANCY            ="7589A046-2D14-49C0-8AC7-FA141E53058C";
    public static final String TOP_TYPE_OF_DRUG                          ="94B44592-5DD6-4998-8498-2EA284148113";
    public static final String TOP_TYPE_OF_SURGICAL_PROCEDURE            ="495BF24F-8AE6-41CB-9499-D490940A8CCE";
    public static final String TOP_TYPE_OF_ANAESTHESIA                   ="2DC5F10B-8C8A-4407-9F05-3342EFD08F3F";
    //Post-Termination Of Pregnancy Counselling
    public static final String TOP_POST_COUNSELLING_RESULT               ="5E2DFDEF-DA59-4F61-AF3E-43FCA7D5CEB5";

    public static final String LDT_AMENDMENT_REASON                      = "B6F2B5FA-689E-4505-97EC-8C1479D19351";

    public static final String DP_PATIENT_INFO_AMENDMENT_REASON          ="DF3C1D7C-3B79-42DA-A7E7-FBFFD47BE9AA";

    public static final String CATE_ID_GIRO_BANK_CODE     = "A4CB167F-5273-EC11-8B81-000C293F0C99";

    /**
     * @description: refresh the master codes into cache
     *
     * @author: Jinhua on 2019/7/26 11:04
     * @param: []
     * @return: void
     */
    public static void refreshCache() {
        MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
        client.refreshCache();
    }

    /**
     * @description: The method to get the category id by constant name
     *
     * @author: Jinhua on 2019/7/29 16:12
     * @param: [cateKey] -- CATE_ID_NATIONALITY
     * @return: java.lang.String -- 1
     */
    public static String getCategoryId(String cateKey){
        try {
            Field field = MasterCodeUtil.class.getDeclaredField(cateKey);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return cateKey;
        }
    }

    /**
     * @description: The method to retrieve Master codes by Category
     *
     * @author: Jinhua on 2019/7/29 17:42
     * @param: [cateId]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.dto.MasterCodeView>
     */
    public static List<MasterCodeView> retrieveByCategory(String cateId) {
        List<MasterCodeView> list = retrieveCateSource(cateId);
        List<MasterCodeView> mcList = IaisCommonUtils.genNewArrayList();
        list.forEach(m ->
            mcList.add(MiscUtil.transferEntityDto(m, MasterCodeView.class))
        );

        return mcList;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Category
     *
     * @author: Jinhua on 2019/7/29 17:44
     * @param: [cateId]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByCate(String cateId) {
        List<MasterCodeView> list = retrieveCateSource(cateId);
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        list.forEach(m ->
            opts.add(new SelectOption(m.getCode(), m.getCodeValue()))
        );

        return opts;
    }

    public static List<String> getCodeKeyByCodeValue(String codeVal){
        List<String> codeKey = IaisCommonUtils.genNewArrayList();
        SearchParam param = new SearchParam(MasterCodeView.class.getName());
        param.addParam("activeFilter", "Yes");
        param.addFilter("codeValFilter", codeVal, true);
        QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
        MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
        SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
        if (sr.getRowCount() > 0) {
            List<MasterCodeView> masterCodeViewList = sr.getRows();
            for (MasterCodeView masterCodeView:masterCodeViewList
                 ) {
                codeKey.add(masterCodeView.getCode());
            }
        }
        return codeKey;
    }

    /**
     * Get code key via category id and code value
     *
     * @param cateId
     * @param codeValue
     * @return
     */
    public static String getCodeKeyByCateIdAndCodeVal(String cateId, String codeValue) {
        String codeKey = null;
        if (StringUtil.isEmpty(cateId)) {
            return codeKey;
        }
        List<MasterCodeView> masterCodes = retrieveByCategory(cateId);
        if (IaisCommonUtils.isNotEmpty(masterCodes)) {
            Optional<String> code = masterCodes.stream()
                    .filter(i -> StringUtil.isEmpty(codeValue) ? StringUtil.isEmpty(i.getCodeValue()) :
                            codeValue.equalsIgnoreCase(i.getCodeValue()))
                    .findAny()
                    .map(MasterCodeView::getCode);
            codeKey = code.orElse(null);
        }
        return codeKey;
    }

    /**
     * @description: The method to get master code value by code
     *
     * @author: Jinhua on 2019/7/29 17:50
     * @param: [code]
     * @return: java.lang.String
     */
    public static String getCodeDesc(String code) {
        String desc = SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                .get(RedisNameSpaceConstant.CACHE_NAME_CODE, code);
        if (StringUtil.isEmpty(desc) && !StringUtil.isEmpty(code)) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.addFilter("codeFilter", code, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
            if (sr.getRowCount() > 0) {
                Date now = new Date();
                int version = -1;
                for (MasterCodeView mc : sr.getRows()) {
                    if (AppConsts.COMMON_STATUS_ACTIVE.equals(mc.getStatus())
                            && now.after(mc.getEffectFrom())
                            && (mc.getEffectTo() == null || now.before(mc.getEffectTo()))) {
                        addMcToCache(mc);
                        return mc.getCodeValue();
                    } else if (mc.getVersion() > version) {
                        desc = mc.getCodeValue();
                        addMcToCache(mc);
                        version = mc.getVersion();
                    }
                }
            } else {
                return "";
            }
        }

        return desc;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Filter
     *
     * @author: Jinhua on 2019/7/29 17:51
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByFilter(String filter) {
        List<MasterCodeView> list = retrieveFilterSource(filter);
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        list.forEach(m ->
            opts.add(new SelectOption(m.getCode(), m.getCodeValue()))
        );

        return opts;
    }

    /**
     * @description: The method to retrieve Master codes by Filter
     *
     * @author: Jinhua on 2019/7/29 17:53
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.dto.MasterCodeView>
     */
    public static List<MasterCodeView> retrieveByFilter(String filter) {
        List<MasterCodeView> list = retrieveFilterSource(filter);
        List<MasterCodeView> mcList = IaisCommonUtils.genNewArrayList();
        list.forEach(m ->
            mcList.add(MiscUtil.transferEntityDto(m, MasterCodeView.class))
        );

        return mcList;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Master Codes
     *
     * @author: Jinhua on 2019/7/29 17:52
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByCodes(String[] codes) {
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (codes == null) {
            return opts;
        }
        for (String c : codes) {
            opts.add(new SelectOption(c, getCodeDesc(c)));
        }

        return opts;
    }

    /******************************************************************************************************************
         Private methods
     ******************************************************************************************************************/
    private static List<MasterCodeView> retrieveCateSource(String cateId) {
        List<MasterCodeView> list = SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                .get(RedisNameSpaceConstant.CACHE_NAME_CATE_MAP, cateId);
        if (list == null) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.setSort(SEQUENCE, SearchParam.ASCENDING);
            param.addParam("activeFilter", "Yes");
            param.addFilter("cateFilter", cateId, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
            if (sr.getRowCount() > 0) {
                Date now = new Date();
                list = sr.getRows();
                list.forEach(m -> {
                    SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                            .set(RedisNameSpaceConstant.CACHE_NAME_CODE, m.getCode(), m.getCodeValue(),
                                    RedisCacheHelper.NOT_EXPIRE);
                });
                SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                        .set(RedisNameSpaceConstant.CACHE_NAME_CATE_MAP, cateId, list,
                        RedisCacheHelper.NOT_EXPIRE);
            } else {
                return IaisCommonUtils.genNewArrayList();
            }
        }

        return list;
    }

    private static List<MasterCodeView> retrieveFilterSource(String filter) {
        List<MasterCodeView> list = SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                .get(MasterCodeHelper.CACHE_NAME_FILTER, filter);
        if (list == null) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.setSort(SEQUENCE, SearchParam.ASCENDING);
            param.addParam("activeFilter", "Yes");
            param.addFilter("filterAttr", filter, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                Date now = new Date();
                list.forEach(m -> {
                    SpringContextHelper.getContext().getBean(RedisCacheHelper.class)
                            .set(RedisNameSpaceConstant.CACHE_NAME_CODE,
                                    m.getCode(), m.getCodeValue(),
                                    RedisCacheHelper.NOT_EXPIRE);
                });
                SpringContextHelper.getContext().getBean(RedisCacheHelper.class).set(MasterCodeHelper.CACHE_NAME_FILTER,
                        filter, list, RedisCacheHelper.NOT_EXPIRE);
            } else {
                return IaisCommonUtils.genNewArrayList();
            }
        }

        return list;
    }

    private static void saveInCache(String cacheName, Map<String, List<MasterCodeView>> conMap) {
        RedisCacheHelper rch = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
        rch.clear(cacheName);
        conMap.entrySet().forEach(ent ->
            rch.set(cacheName, ent.getKey(), ent.getValue(), RedisCacheHelper.NOT_EXPIRE)
        );
    }

    private static void addMcToCache(MasterCodeView mc) {
        MasterCodeHelper rch = SpringContextHelper.getContext().getBean(MasterCodeHelper.class);
        rch.addMcToCache(mc);
    }

    public static String getDecByCateIdAndCodeValue(String cateId,String codeValue){
        if(StringUtil.isEmpty(codeValue)){ return "";}
        for (MasterCodeView masterCodeView : retrieveCateSource(cateId)) {
            if(codeValue.equalsIgnoreCase(masterCodeView.getCodeValue())){
                return StringUtil.isEmpty(masterCodeView.getDescription()) ? codeValue : masterCodeView.getDescription();
            }
        }
        return codeValue;
    }

    // keys.size = values.size
    public static <K,V> Map<K,V> listKeyAndValueMap(List<K> keys,List<V> values){
        Map<K,V> kvMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(keys) && IaisCommonUtils.isNotEmpty(values)){
            for (int i = 0; i < keys.size(); i++) {
                kvMap.put(keys.get(i),values.get(i));
            }
        }
        return kvMap;
    }

    private MasterCodeUtil() {throw new IllegalStateException("Util class");}
}
