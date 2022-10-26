package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldAllEditableJudger;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_PENDING_AFC_SELECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ACTION_LOAD_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.HAVE_SUITABLE_DRAFT_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_LAST_TWO_ROUND_AFC_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_LAST_TWO_ROUND_AFC_SET;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_NAV_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_NAV_PAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

@Delegator("bsbAfcSelectionDelegator")
@Slf4j
@RequiredArgsConstructor
public class BsbAfcSelectionDelegator {

    private final RfiService rfiService;
    private final RfiClient rfiClient;
    private final DraftClient draftClient;
    private final FacilityRegisterClient facilityRegisterClient;
    private static final String KEY_ROUTE = "route";
    private static final String KEY_PRINT_MASK_PARAM = "printAfcSelection";
    private static final String KEY_PRINT_MASKED_ID = "printAfcSelectionId";
    public static final String KEY_CERTIFICATION_APP_ID = "cerAppId";
    public static final String KEY_CAN_SAVE_DRAFT_JUDGE = "canSaveDraftJudge";

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(NODE_NAME_AFC);
        request.getSession().removeAttribute(KEY_CAN_SAVE_DRAFT_JUDGE);
        request.getSession().removeAttribute(KEY_APP_ID);
        rfiService.clearAndSetAppIdInSession(request);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_PENDING_AFC_SELECTION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String doRfi = (String) ParamUtil.getSessionAttr(request ,KEY_CONFIRM_RFI);
        boolean isRfi = (StringUtils.hasLength(doRfi)) && KEY_CONFIRM_RFI_Y.equals(doRfi);

        // check if we are doing editing
        boolean failRetrieveEditData = true;
        String finalAppId = "";
        if(isRfi) {
            finalAppId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            ParamUtil.setSessionAttr(request, KEY_CAN_SAVE_DRAFT_JUDGE, false);
        } else {
            String maskedAppId = request.getParameter(KEY_APP_ID);
            if (StringUtils.hasLength(maskedAppId)) {
                String appId = MaskUtil.unMaskValue(KEY_CERTIFICATION_APP_ID, maskedAppId);
                if (appId != null && !maskedAppId.equals(appId)) {
                    finalAppId = appId;
                }
            }
            ParamUtil.setSessionAttr(request, KEY_CAN_SAVE_DRAFT_JUDGE, true);
        }

        if(StringUtils.hasLength(finalAppId)) {
            ResponseDto<FacilityAfcDto> resultDto  = facilityRegisterClient.getFacilityAfcDtoByApplicationId(finalAppId);
            if(resultDto.ok()) {
                failRetrieveEditData = false;
                FacilityAfcDto facilityAfcDto = resultDto.getEntity();
                String hasDraft = facilityAfcDto.getHasDraft();
                if(MasterCodeConstants.YES.equals(hasDraft)) {
                    ParamUtil.setRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA, true);
                }
                ParamUtil.setSessionAttr(request, NODE_NAME_AFC, facilityAfcDto);
                ParamUtil.setSessionAttr(request, KEY_APP_ID, finalAppId);
            }
        }

        if (failRetrieveEditData) {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }

    public void preAfcSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String applicationId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        String actionLoadDraft = ParamUtil.getString(request, ACTION_LOAD_DRAFT);
        if(MasterCodeConstants.NO.equals(actionLoadDraft)) {
            //delete draft which record in database
            draftClient.doRemoveDraftByDraftAppId(applicationId);
            //clear session
            ParamUtil.setSessionAttr(request, NODE_NAME_AFC, null);
        }

        FacilityAfcDto facilityAfcDto = getFacilityAfcDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facilityAfcDto.retrieveValidationResult());
        }

        Set<String> last2AfcSet = facilityAfcDto.getLast2AfcSet();
        if(CollectionUtils.isEmpty(last2AfcSet)){
            last2AfcSet = facilityRegisterClient.getLast2CertificationAfcByApplicationId(applicationId);
            facilityAfcDto.setLast2AfcSet(last2AfcSet);
            facilityAfcDto.setCertAppId(applicationId);
        }
        String last2AfcJson = JsonUtil.parseToJson(last2AfcSet);
        ParamUtil.setRequestAttr(request, KEY_LAST_TWO_ROUND_AFC_SET, last2AfcSet);
        ParamUtil.setRequestAttr(request, KEY_LAST_TWO_ROUND_AFC_JSON, last2AfcJson);
        ParamUtil.setSessionAttr(request, NODE_NAME_AFC, facilityAfcDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_AFC, MasterCodeHolder.APPROVED_FACILITY_CERTIFIER.allOptions());
    }

    public void doAfcSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAfcDto afcDto = getFacilityAfcDto(request);
        afcDto.reqObjectMapping(request, new FieldAllEditableJudger());

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if(!KEY_NAV_DRAFT.equals(actionType)) {
            if(KEY_SUBMIT.equals(actionType)) {
                if(!afcDto.doValidation()) {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setRequestAttr(request, KEY_ROUTE, KEY_NAV_PAGE);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_ROUTE, KEY_SUBMIT);
                }
            } else {
                ParamUtil.setRequestAttr(request, KEY_ROUTE, actionType);
            }
        } else {
            ParamUtil.setRequestAttr(request, KEY_ROUTE, KEY_NAV_DRAFT);
        }
    }

    public void saveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAfcDto facilityAfcDto = getFacilityAfcDto(request);
        facilityAfcDto.setHasDraft(MasterCodeConstants.YES);
        facilityRegisterClient.saveNewCertFacilityAfcDraft(facilityAfcDto);

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public void saveAfcSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAfcDto facilityAfcDto = getFacilityAfcDto(request);
        String confirmRfi = (String) ParamUtil.getSessionAttr(request, KEY_CONFIRM_RFI);
        if (confirmRfi != null && confirmRfi.equals(KEY_CONFIRM_RFI_Y)) {
            //save rfi data
            String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
            ResponseDto<AppMainInfo> resultDto = rfiClient.saveApplicantCertificationAfc(facilityAfcDto, rfiDataId);
            if(resultDto.ok()){
                AppMainInfo appMainInfo = resultDto.getEntity();
                ParamUtil.setRequestAttr(request, "appNo", appMainInfo.getAppNo());
                ParamUtil.setRequestAttr(request, "appDt", appMainInfo.getDate());
            }
        } else {
            ResponseDto<AppMainInfo> resultDto = facilityRegisterClient.saveNewCertFacilityAfc(facilityAfcDto);
            if(resultDto.ok()){
                AppMainInfo appMainInfo = resultDto.getEntity();
                ParamUtil.setRequestAttr(request, "appNo", appMainInfo.getAppNo());
                ParamUtil.setRequestAttr(request, "appDt", appMainInfo.getDate());
            }
        }

        String maskForPrint = MaskUtil.maskValue(KEY_PRINT_MASK_PARAM, String.valueOf(facilityAfcDto.hashCode()));
        ParamUtil.setRequestAttr(request, KEY_PRINT_MASKED_ID, maskForPrint);
    }

    public void print(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedId = ParamUtil.getString(request, "printId");
        boolean allowToPrint = false;
        if (StringUtils.hasLength(maskedId)) {
            MaskUtil.unMaskValue(KEY_PRINT_MASK_PARAM, maskedId);
            // if it can not be unmasked, an exception is thrown
            allowToPrint = true;
        }
        if (!allowToPrint) {
            throw new IaisRuntimeException("Invalid mask key, don't allow to print");
        }
    }

    public FacilityAfcDto getFacilityAfcDto(HttpServletRequest request) {
        FacilityAfcDto facilityAfcDto = (FacilityAfcDto) ParamUtil.getSessionAttr(request, NODE_NAME_AFC);
        return facilityAfcDto == null ? new FacilityAfcDto() : facilityAfcDto;
    }
}
