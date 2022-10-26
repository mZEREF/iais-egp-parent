package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.RefreshableDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.util.RequestObjectMappingUtil;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService.KEY_ORG_ADDRESS;


@Slf4j
@ToString
@Getter
@Setter
public class FacilityProfileInfo implements RefreshableDocDto, Serializable {
    private String facilityEntityId;

    private String facName;

    private String facType;

    private String facTypeDetails;

    private String sameAddress;

    private String block;

    private String addressType;

    private String streetName;

    private String floor;

    private String unitNo;

    private String postalCode;

    private String building;

    private String facilityProtected;

    /* docs already saved in DB, key is repoId */
    private Map<String, DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private Map<String, NewDocInfo> newDocMap;

    private String inChargePersonName;

    private String inChargePersonDesignation;

    private String inChargePersonEmail;

    private String inChargePersonContactNo;

    private String opvSabin1IM;
    private String opvSabin2IM;
    private String opvSabin3IM;
    private String opvSabin1IMExpectedDestructDt;
    private String opvSabin2IMExpectedDestructDt;
    private String opvSabin3IMExpectedDestructDt;
    private String opvSabin1IMRetentionReason;
    private String opvSabin2IMRetentionReason;
    private String opvSabin3IMRetentionReason;

    private String opvSabin1PIM;
    private String opvSabin2PIM;
    private String opvSabin3PIM;
    private String opvSabin1PIMRiskLevel;
    private String opvSabin2PIMRiskLevel;
    private String opvSabin3PIMRiskLevel;
    private String opvSabin1PIMRetentionReason;
    private String opvSabin2PIMRetentionReason;
    private String opvSabin3PIMRetentionReason;


    public FacilityProfileInfo() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
    }


    @Override
    public Map<String, byte[]> prepare4Saving() {
        return Maps.transformValues(newDocMap, i -> i.getMultipartFile().getBytes());
    }

    @Override
    public void refreshAfterSave(Map<String, String> idMap) {
        RefreshableDocDto.refreshDocMap(newDocMap, savedDocMap, idMap);
    }


    public List<DocMeta> getAllFilesMeta() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.newDocMap.size() + this.savedDocMap.size());
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.savedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        return metaDtoList;
    }



    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_FAC_NAME = "facName";
    private static final String KEY_FACILITY_TYPE = "facType";
    private static final String KEY_FACILITY_TYPE_DETAILS = "facTypeDetails";
    private static final String KEY_IS_SAME_ADDRESS_AS_COMPANY = "isSameAddress";
    private static final String KEY_BLOCK = "block";
    private static final String KEY_ADDRESS_TYPE = "addressType";
    private static final String KEY_STREET_NAME = "streetName";
    private static final String KEY_FLOOR = "floor";
    private static final String KEY_UNIT_NO = "unitNo";
    private static final String KEY_POSTAL_CODE = "postalCode";
    private static final String KEY_BUILDING_NAME = "buildingName";
    private static final String KEY_IS_PROTECTED_PLACE = "protectedPlace";

    private static final String KEY_IN_CHARGE_PERSON_NAME = "inChargePersonName";
    private static final String KEY_IN_CHARGE_PERSON_DESIGNATION = "inChargePersonDesignation";
    private static final String KEY_IN_CHARGE_PERSON_EMAIL = "inChargePersonEmail";
    private static final String KEY_IN_CHARGE_PERSON_CONTACT_NO = "inChargePersonContactNo";

    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1IM = "opvSabin1IM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2IM = "opvSabin2IM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3IM = "opvSabin3IM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1IM_DESTRUCT_DT = "opvSabin1IMExpectedDestructDt";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2IM_DESTRUCT_DT = "opvSabin2IMExpectedDestructDt";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3IM_DESTRUCT_DT = "opvSabin3IMExpectedDestructDt";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1IM_RETENTION_REASON = "opvSabin1IMRetentionReason";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2IM_RETENTION_REASON = "opvSabin2IMRetentionReason";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3IM_RETENTION_REASON = "opvSabin3IMRetentionReason";

    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1PIM = "opvSabin1PIM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2PIM = "opvSabin2PIM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3PIM = "opvSabin3PIM";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RISK_LEVEL = "opvSabin1PIMRiskLevel";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RISK_LEVEL = "opvSabin2PIMRiskLevel";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RISK_LEVEL = "opvSabin3PIMRiskLevel";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RETENTION_REASON = "opvSabin1PIMRetentionReason";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RETENTION_REASON = "opvSabin2PIMRetentionReason";
    private static final String KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RETENTION_REASON = "opvSabin3PIMRetentionReason";


    private static final String KEY_GAZETTE = "gazetteOrder";

    private static final String SEPARATOR = "--v--";


    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger, String idx, String facClassification, List<String> activityTypes) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(facClassification);
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(activityTypes.get(0));

        if (editableJudger.editable(KEY_FAC_NAME)) {
            this.setFacName(mulReq.getParameter(KEY_FAC_NAME + SEPARATOR + idx));
        }
        if (editableJudger.editable(KEY_FACILITY_TYPE)) {
            this.setFacType(ParamUtil.getString(mulReq, KEY_FACILITY_TYPE + SEPARATOR + idx));
        }
        if (MasterCodeConstants.FACILITY_TYPE_OTHERS.equals(this.getFacType()) && editableJudger.editable(KEY_FACILITY_TYPE_DETAILS)) {
            this.setFacTypeDetails(ParamUtil.getString(mulReq, KEY_FACILITY_TYPE_DETAILS + SEPARATOR + idx));
        }

        if (editableJudger.editable(KEY_IS_SAME_ADDRESS_AS_COMPANY)) {
            String sameAddressAsCompany = ParamUtil.getString(mulReq, KEY_IS_SAME_ADDRESS_AS_COMPANY + SEPARATOR + idx);
            if (MasterCodeConstants.YES.equals(sameAddressAsCompany)) {
                if (!sameAddressAsCompany.equals(this.sameAddress)) {
                    // load company address and set to this DTO
                    useOrganizationAddress4Facility(request);
                }
                // do nothing, if current address is already the same as company
            } else if (MasterCodeConstants.NO.equals(sameAddressAsCompany)) {
                this.setAddressType(ParamUtil.getString(mulReq, KEY_ADDRESS_TYPE + SEPARATOR + idx));
                this.setBlock(ParamUtil.getString(mulReq, KEY_BLOCK + SEPARATOR + idx));
                this.setStreetName(ParamUtil.getString(mulReq, KEY_STREET_NAME + SEPARATOR + idx));
                this.setFloor(ParamUtil.getString(mulReq, KEY_FLOOR + SEPARATOR + idx));
                this.setUnitNo(ParamUtil.getString(mulReq, KEY_UNIT_NO + SEPARATOR + idx));
                this.setPostalCode(ParamUtil.getString(mulReq, KEY_POSTAL_CODE + SEPARATOR + idx));
                this.setBuilding(ParamUtil.getString(mulReq, KEY_BUILDING_NAME + SEPARATOR + idx));
            }
            this.setSameAddress(sameAddressAsCompany);
        }

        if (editableJudger.editable(KEY_IS_PROTECTED_PLACE)) {
            this.setFacilityProtected(ParamUtil.getString(mulReq, KEY_IS_PROTECTED_PLACE + SEPARATOR + idx));
        }

        // read new uploaded files if the facility is gazetted as a protected place
        if (MasterCodeConstants.YES.equals(this.facilityProtected) && !isRf) {
            List<MultipartFile> files = mulReq.getFiles(KEY_GAZETTE + SEPARATOR + idx);
            Date currentDate = new Date();
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            for (MultipartFile f : files) {
                readNewUploadedFile(f, loginContext.getUserId(), currentDate);
            }
        }

        if (editableJudger.editable(KEY_IN_CHARGE_PERSON_NAME)) {
            this.setInChargePersonName(RequestObjectMappingUtil.sneakyParamVal(mulReq.getParameter(KEY_IN_CHARGE_PERSON_NAME + SEPARATOR + idx), isRf));
        }
        if (editableJudger.editable(KEY_IN_CHARGE_PERSON_DESIGNATION)) {
            this.setInChargePersonDesignation(RequestObjectMappingUtil.sneakyParamVal(mulReq.getParameter(KEY_IN_CHARGE_PERSON_DESIGNATION + SEPARATOR + idx), isRf));
        }
        if (editableJudger.editable(KEY_IN_CHARGE_PERSON_EMAIL)) {
            this.setInChargePersonEmail(RequestObjectMappingUtil.sneakyParamVal(mulReq.getParameter(KEY_IN_CHARGE_PERSON_EMAIL + SEPARATOR + idx), isRf));
        }
        if (editableJudger.editable(KEY_IN_CHARGE_PERSON_CONTACT_NO)) {
            this.setInChargePersonContactNo(RequestObjectMappingUtil.sneakyParamVal(mulReq.getParameter(KEY_IN_CHARGE_PERSON_CONTACT_NO + SEPARATOR + idx), isRf));
        }

        if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1IM)) {
            this.setOpvSabin1IM(RequestObjectMappingUtil.sneakyParamVal(mulReq.getParameter(KEY_INVENTORY_INFO_OPV_SABIN_1IM + SEPARATOR + idx), isPvRf));
        }
        if (MasterCodeConstants.YES.equals(this.opvSabin1IM)) {
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1IM_DESTRUCT_DT)) {
                this.setOpvSabin1IMExpectedDestructDt(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_1IM_DESTRUCT_DT + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1IM_RETENTION_REASON)) {
                this.setOpvSabin1IMRetentionReason(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_1IM_RETENTION_REASON + SEPARATOR + idx));
            }
        }
        this.setOpvSabin2IM(MasterCodeConstants.NO);
        if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3IM)) {
            this.setOpvSabin3IM(RequestObjectMappingUtil.sneakyParamVal(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3IM + SEPARATOR + idx), isPvRf));
        }
        if (MasterCodeConstants.YES.equals(this.opvSabin3IM)) {
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3IM_DESTRUCT_DT)) {
                this.setOpvSabin3IMExpectedDestructDt(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3IM_DESTRUCT_DT + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3IM_RETENTION_REASON)) {
                this.setOpvSabin3IMRetentionReason(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3IM_RETENTION_REASON + SEPARATOR + idx));
            }
        }
        if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1PIM)) {
            this.setOpvSabin1PIM(RequestObjectMappingUtil.sneakyParamVal(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_1PIM + SEPARATOR + idx), isPvRf));
        }
        if (MasterCodeConstants.YES.equals(this.opvSabin1PIM)) {
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RISK_LEVEL)) {
                this.setOpvSabin1PIMRiskLevel(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RISK_LEVEL + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RETENTION_REASON)) {
                this.setOpvSabin1PIMRetentionReason(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_1PIM_RETENTION_REASON + SEPARATOR + idx));
            }
        }
        if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_2PIM)) {
            this.setOpvSabin2PIM(RequestObjectMappingUtil.sneakyParamVal(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_2PIM + SEPARATOR + idx), isPvRf));
        }
        if (MasterCodeConstants.YES.equals(this.opvSabin2PIM)) {
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RISK_LEVEL)) {
                this.setOpvSabin2PIMRiskLevel(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RISK_LEVEL + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RETENTION_REASON)) {
                this.setOpvSabin2PIMRetentionReason(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_2PIM_RETENTION_REASON + SEPARATOR + idx));
            }
        }
        if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3PIM)) {
            this.setOpvSabin3PIM(RequestObjectMappingUtil.sneakyParamVal(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3PIM + SEPARATOR + idx), isPvRf));
        }
        if (MasterCodeConstants.YES.equals(this.opvSabin3PIM)) {
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RISK_LEVEL)) {
                this.setOpvSabin3PIMRiskLevel(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RISK_LEVEL + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RETENTION_REASON)) {
                this.setOpvSabin3PIMRetentionReason(ParamUtil.getString(mulReq, KEY_INVENTORY_INFO_OPV_SABIN_3PIM_RETENTION_REASON + SEPARATOR + idx));
            }
        }
    }




    private void useOrganizationAddress4Facility(HttpServletRequest request) {
        OrgAddressInfo orgAddressInfo = (OrgAddressInfo) request.getSession().getAttribute(KEY_ORG_ADDRESS);
        this.setPostalCode(orgAddressInfo.getPostalCode());
        this.setAddressType(orgAddressInfo.getAddressType());
        this.setBlock(orgAddressInfo.getBlockNo());
        this.setFloor(orgAddressInfo.getFloor());
        this.setUnitNo(orgAddressInfo.getUnitNo());
        this.setStreetName(orgAddressInfo.getStreet());
        this.setBuilding(orgAddressInfo.getBuilding());
    }

    private void readNewUploadedFile(MultipartFile f, String submitUser, Date submitDate) {
        if (f.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("File [{}] is empty, ignore it", LogUtil.escapeCrlf(f.getOriginalFilename()));
            }
        } else {
            NewDocInfo newDocInfo = new NewDocInfo();
            String tmpId = DocConstants.DOC_TYPE_FACILITY_GAZETTE_ORDER + f.getSize() + System.nanoTime();
            newDocInfo.setTmpId(tmpId);
            newDocInfo.setDocType(DocConstants.DOC_TYPE_FACILITY_GAZETTE_ORDER);
            newDocInfo.setFilename(f.getOriginalFilename());
            newDocInfo.setSize(f.getSize());
            newDocInfo.setSubmitDate(submitDate);
            newDocInfo.setSubmitBy(submitUser);
            byte[] bytes = new byte[0];
            try {
                bytes = f.getBytes();
            } catch (IOException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Fail to read bytes of file [{}], tmpId {}", LogUtil.escapeCrlf(f.getOriginalFilename()), tmpId);
                }
            }
            ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
            newDocInfo.setMultipartFile(multipartFile);
            this.newDocMap.put(tmpId, newDocInfo);
        }
    }
}
