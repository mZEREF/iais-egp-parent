package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * IuiTreatmentSubsidiesDelegator
 *
 * @author zhixing
 * @date 2021/10/28
 */

@Delegator("iuiTreatmentSubsidiesDelegator")
@Slf4j
public class IuiTreatmentSubsidiesDelegator extends CommonDelegator {
    @Autowired
    private ArFeClient arFeClient;

    public static final String PLEASE_INDICATE_IUI_CO_FUNDING = "pleaseIndicateIuiCoFunding";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setSessionAttr(request,PLEASE_INDICATE_IUI_CO_FUNDING, (Serializable) MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.PLEASE_INDICATE_IUI_CO_FUNDING));
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);


    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
    }

    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto=arSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto();
        if(iuiTreatmentSubsidiesDto == null) {
            iuiTreatmentSubsidiesDto = new IuiTreatmentSubsidiesDto();
        }
        if(StringUtil.isEmpty(iuiTreatmentSubsidiesDto.getArtCoFunding())){
            iuiTreatmentSubsidiesDto.setArtCoFunding("PICF001");
        }
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        List<IuiTreatmentSubsidiesDto> oldIuiTreatmentSubsidiesDtos = arFeClient.getIuiTreatmentSubsidiesDtosByPhc(cycleDto.getPatientCode(), cycleDto.getHciCode(), cycleDto.getCycleType()).getEntity();
        int iuiCount=oldIuiTreatmentSubsidiesDtos.size();
        ParamUtil.setRequestAttr(bpc.request, "iuiCount", iuiCount);
        arSuperDataSubmissionDto.setIuiTreatmentSubsidiesDto(iuiTreatmentSubsidiesDto);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override

    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto=arSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto();
        if(iuiTreatmentSubsidiesDto == null) {
            iuiTreatmentSubsidiesDto = new IuiTreatmentSubsidiesDto();
        }
            String pleaseIndicateIui = ParamUtil.getString(bpc.request, "pleaseIndicateIui");
            String thereAppeal = ParamUtil.getRequestString(bpc.request, "thereAppeal");
            iuiTreatmentSubsidiesDto.setArtCoFunding(pleaseIndicateIui);
            iuiTreatmentSubsidiesDto.setThereAppeal(Boolean.parseBoolean(thereAppeal));
            arSuperDataSubmissionDto.setIuiTreatmentSubsidiesDto(iuiTreatmentSubsidiesDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(iuiTreatmentSubsidiesDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();

            String crud_action_type = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);

            if ("confirm".equals(crud_action_type)) {
                errorMap = validationResult.retrieveAll();
                verifyRfcCommon(bpc.request, errorMap);
                valRFC(bpc.request, iuiTreatmentSubsidiesDto);
            }
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                return;
            }
            if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
                valRFC(bpc.request,iuiTreatmentSubsidiesDto);
            }
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }
    protected void valRFC(HttpServletRequest request, IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto()!= null && iuiTreatmentSubsidiesDto.equals(arOldSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
