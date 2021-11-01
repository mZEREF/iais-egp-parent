package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * PgoCycleStageDelegator
 *
 * @author junyu
 * @date 2021/10/28
 */
@Delegator("preimplantationDelegator")
@Slf4j
public class PgtCycleStageDelegator extends CommonDelegator{
    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Preimplantation Genetic Testing");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        arSuperDataSubmissionDto.setPgtStageDto(new PgtStageDto());
        arSuperDataSubmissionDto.getPgtStageDto().setIsPgtMNon(1);
        arSuperDataSubmissionDto.getPgtStageDto().setIsPgtCoFunding(0);

        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PgtStageDto pgtStageDto=arSuperDataSubmissionDto.getPgtStageDto();
        pgtStageDto.setIsPgtM(0);
        pgtStageDto.setIsPgtSr(0);
        pgtStageDto.setIsPgtA(0);
        pgtStageDto.setIsPtt(0);
        pgtStageDto.setIsOtherPgt(0);
        pgtStageDto.setIsPgtMDsld(0);
        pgtStageDto.setIsPgtMWithHla(0);
        pgtStageDto.setIsPgtMNon(0);
        pgtStageDto.setIsPgtAAma(0);
        pgtStageDto.setIsPgtATomrif(0);
        pgtStageDto.setIsPgtATomrpl(0);

        HttpServletRequest request=bpc.request;
        String isPgtM =  ParamUtil.getString(request, "isPgtM");
        String isPgtSr =  ParamUtil.getString(request, "isPgtSr");
        String isPgtA =  ParamUtil.getString(request, "isPgtA");
        String isPtt =  ParamUtil.getString(request, "isPtt");
        String isOtherPgt =  ParamUtil.getString(request, "isOtherPgt");


        if( "on".equals(isPgtM)){
            pgtStageDto.setIsPgtM(1);
            String isPgtMDsld =  ParamUtil.getString(request, "isPgtMDsld");
            if("on".equals(isPgtMDsld)){
                pgtStageDto.setIsPgtMDsld(1);
            }
            String isPgtMWithHla =  ParamUtil.getString(request, "isPgtMWithHla");
            if("on".equals(isPgtMWithHla)){
                pgtStageDto.setIsPgtMWithHla(1);
            }
            String isPgtMNon =  ParamUtil.getString(request, "isPgtMNon");
            if("on".equals(isPgtMNon)){
                pgtStageDto.setIsPgtMNon(1);
            }
            String pgtMRefNo = ParamUtil.getString(request, "pgtMRefNo");
            pgtStageDto.setPgtMRefNo(pgtMRefNo);
            String pgtMCondition = ParamUtil.getString(request, "pgtMCondition");
            pgtStageDto.setPgtMCondition(pgtMCondition);
        }
        if("on".equals(isPgtSr)){
            pgtStageDto.setIsPgtSr(1);
            String pgtSrCondition = ParamUtil.getString(request, "pgtSrCondition");
            pgtStageDto.setPgtSrCondition(pgtSrCondition);

        }

        if("on".equals(isPgtA)){
            pgtStageDto.setIsPgtA(1);
            String isPgtAAma =  ParamUtil.getString(request, "isPgtAAma");
            if("on".equals(isPgtAAma)){
                pgtStageDto.setIsPgtAAma(1);
            }
            String isPgtATomrif =  ParamUtil.getString(request, "isPgtATomrif");
            if("on".equals(isPgtATomrif)){
                pgtStageDto.setIsPgtATomrif(1);
            }
            String isPgtATomrpl =  ParamUtil.getString(request, "isPgtATomrpl");
            if("on".equals(isPgtATomrpl)){
                pgtStageDto.setIsPgtATomrpl(1);
            }
            String pgtAResult =  ParamUtil.getString(request, "pgtAResult");
            pgtStageDto.setPgtAResult(pgtAResult);
            if( "Abnormal".equals(pgtAResult)){
                String pgtACondition = ParamUtil.getString(request, "pgtACondition");
                pgtStageDto.setPgtACondition(pgtACondition);
            }

        }

        if("on".equals(isPtt)){
            pgtStageDto.setIsPtt(1);
            String pttCondition = ParamUtil.getString(request, "pttCondition");
            pgtStageDto.setPttCondition(pttCondition);

        }

        if("on".equals(isOtherPgt)){
            pgtStageDto.setIsOtherPgt(1);
        }

        String isPgtCoFunding = ParamUtil.getString(request, "isPgtCoFunding");
        if("0".equals(isPgtCoFunding)){
            pgtStageDto.setIsPgtCoFunding(0);
        }
        if("1".equals(isPgtCoFunding)){
            pgtStageDto.setIsPgtCoFunding(1);
        }
        String isEmbryosBiopsiedLocal = ParamUtil.getString(request, "isEmbryosBiopsiedLocal");
        if("0".equals(isEmbryosBiopsiedLocal)){
            pgtStageDto.setIsEmbryosBiopsiedLocal(0);
            String otherEmbryosBiopsiedAddr = ParamUtil.getString(request, "otherEmbryosBiopsiedAddr");
            pgtStageDto.setOtherEmbryosBiopsiedAddr(otherEmbryosBiopsiedAddr);
        }
        if("1".equals(isEmbryosBiopsiedLocal)){
            pgtStageDto.setIsEmbryosBiopsiedLocal(1);
        }
        String isBiopsyLocal = ParamUtil.getString(request, "isBiopsyLocal");
        if("0".equals(isBiopsyLocal)){
            pgtStageDto.setIsBiopsyLocal(0);
            String otherBiopsyAddr = ParamUtil.getString(request, "otherBiopsyAddr");
            pgtStageDto.setOtherBiopsyAddr(otherBiopsyAddr);
        }
        if("1".equals(isBiopsyLocal)){
            pgtStageDto.setIsBiopsyLocal(1);
        }

        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(pgtStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");

    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
