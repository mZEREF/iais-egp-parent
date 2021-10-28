package sg.gov.moh.iais.egp.bsb.dto.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalProfileDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String batId;
        private String batName;
        private String prodMaxVolumeLitres;
        private String lspMethod;
        private String procurementMode;
        private String facilityNameOfTransfer;
        private String expectedDateOfImport;
        private String contactPersonNameOfTransfer;
        private String impCtcPersonNo;
        private String contactPersonEmailOfTransfer;
        private String transferFacAddr1;
        private String transferFacAddr2;
        private String transferFacAddr3;
        private String transferCountry;
        private String transferCity;
        private String transferState;
        private String transferPostalCode;
        private String courierServiceProviderName;
        private String remarks;
    }

    private List<BATInfo> batInfos;

    private String schedule;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ApprovalProfileDto() {
        batInfos = new ArrayList<>();
        BATInfo batInfo = new BATInfo();
        batInfos.add(batInfo);
    }

    public ApprovalProfileDto(String schedule) {
        this();
        this.schedule = schedule;
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("approvalAppFeignClient", "validateApprovalProfile", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public List<BATInfo> getBatInfos() {
        return new ArrayList<>(batInfos);
    }

    public void clearBatInfos() {
        this.batInfos.clear();
    }

    public void addBatInfo(BATInfo info) {
        this.batInfos.add(info);
    }

    public void setBatInfos(List<BATInfo> batInfos) {
        this.batInfos = new ArrayList<>(batInfos);
    }

    public ValidationResultDto getValidationResultDto() {
        return validationResultDto;
    }

    public void setValidationResultDto(ValidationResultDto validationResultDto) {
        this.validationResultDto = validationResultDto;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR = "--v--";
    private static final String KEY_SECTION_AMT = "sectionAmt";
    private static final String KEY_PREFIX_BAT_ID = "batId";
    private static final String KEY_PREFIX_BAT_NAME = "batName";
    private static final String KEY_PROD_MAX_VOLUME_LITRES = "prodMaxVolumeLitres";
    private static final String KEY_LSP_METHOD = "lspMethod";
    private static final String KEY_PROCUREMENT_MODE = "procurementMode";
    private static final String KEY_FACILITY_NAME_OF_TRANSFER = "facilityNameOfTransfer";
    private static final String KEY_EXPECTED_DATE_OF_IMPORT = "expectedDateOfImport";
    private static final String KEY_CONTACT_PERSON_NAME_OF_TRANSFER = "contactPersonNameOfTransfer";
    private static final String KEY_IMP_CTC_PERSON_NO = "impCtcPersonNo";
    private static final String KEY_CONTACT_PERSON_EMAIL_OF_TRANSFER = "contactPersonEmailOfTransfer";
    private static final String KEY_TRANSFER_FAC_ADDR_1 = "transferFacAddr1";
    private static final String KEY_TRANSFER_FAC_ADDR_2 = "transferFacAddr2";
    private static final String KEY_TRANSFER_FAC_ADDR_3 = "transferFacAddr3";
    private static final String KEY_TRANSFER_COUNTRY = "transferCountry";
    private static final String KEY_TRANSFER_CITY = "transferCity";
    private static final String KEY_TRANSFER_STATE = "transferState";
    private static final String KEY_TRANSFER_POSTAL_CODE = "transferPostalCode";
    private static final String KEY_COURIER_SERVICE_PROVIDER_NAME = "courierServiceProviderName";
    private static final String KEY_REMARKS = "remarks";

    public void reqObjMapping(HttpServletRequest request) {
        clearBatInfos();
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        for (int i = 0; i < amt; i++) {
            BATInfo info = new BATInfo();
            info.setBatId(ParamUtil.getString(request, KEY_PREFIX_BAT_ID + SEPARATOR +i));
            info.setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME + SEPARATOR +i));
            info.setProdMaxVolumeLitres(ParamUtil.getString(request, KEY_PROD_MAX_VOLUME_LITRES + SEPARATOR +i));
            info.setLspMethod(ParamUtil.getString(request, KEY_LSP_METHOD + SEPARATOR +i));
            info.setProcurementMode(ParamUtil.getString(request, KEY_PROCUREMENT_MODE + SEPARATOR +i));
            info.setFacilityNameOfTransfer(ParamUtil.getString(request, KEY_FACILITY_NAME_OF_TRANSFER + SEPARATOR +i));
            info.setExpectedDateOfImport(ParamUtil.getString(request, KEY_EXPECTED_DATE_OF_IMPORT + SEPARATOR +i));
            info.setContactPersonNameOfTransfer(ParamUtil.getString(request, KEY_CONTACT_PERSON_NAME_OF_TRANSFER + SEPARATOR +i));
            info.setImpCtcPersonNo(ParamUtil.getString(request, KEY_IMP_CTC_PERSON_NO + SEPARATOR +i));
            info.setContactPersonEmailOfTransfer(ParamUtil.getString(request, KEY_CONTACT_PERSON_EMAIL_OF_TRANSFER + SEPARATOR +i));
            info.setTransferFacAddr1(ParamUtil.getString(request, KEY_TRANSFER_FAC_ADDR_1 + SEPARATOR +i));
            info.setTransferFacAddr2(ParamUtil.getString(request, KEY_TRANSFER_FAC_ADDR_2 + SEPARATOR +i));
            info.setTransferFacAddr3(ParamUtil.getString(request, KEY_TRANSFER_FAC_ADDR_3 + SEPARATOR +i));
            info.setTransferCountry(ParamUtil.getString(request, KEY_TRANSFER_COUNTRY + SEPARATOR +i));
            info.setTransferCity(ParamUtil.getString(request, KEY_TRANSFER_CITY + SEPARATOR +i));
            info.setTransferState(ParamUtil.getString(request, KEY_TRANSFER_STATE + SEPARATOR +i));
            info.setTransferPostalCode(ParamUtil.getString(request, KEY_TRANSFER_POSTAL_CODE + SEPARATOR +i));
            info.setCourierServiceProviderName(ParamUtil.getString(request, KEY_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +i));
            info.setRemarks(ParamUtil.getString(request, KEY_REMARKS + SEPARATOR +i));
            addBatInfo(info);
        }
    }
}
