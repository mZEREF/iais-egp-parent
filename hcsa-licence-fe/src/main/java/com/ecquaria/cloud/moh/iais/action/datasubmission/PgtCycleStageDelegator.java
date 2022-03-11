package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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


    @Autowired
    private DpFeClient dpFeClient;

    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Preimplantation Genetic Testing");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getPgtStageDto()==null){
            arSuperDataSubmissionDto.setPgtStageDto(new PgtStageDto());
            arSuperDataSubmissionDto.getPgtStageDto().setIsPgtMNon(1);
            arSuperDataSubmissionDto.getPgtStageDto().setIsPgtCoFunding(0);
            arSuperDataSubmissionDto.getPgtStageDto().setIsThereAppeal(0);
        }
        initPgtCount(arSuperDataSubmissionDto,bpc.request);


        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }

    public void initPgtCount(ArSuperDataSubmissionDto arSuperDataSubmissionDto,HttpServletRequest request) {
        String patientCode=arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode();

        List<PgtStageDto> oldPgtList=dpFeClient.listPgtStageByPatientCode(patientCode).getEntity();
        int countNo =0;
        if(oldPgtList!=null){
            for (PgtStageDto pgt:oldPgtList
            ) {
                if(pgt.getIsPgtMEbt()+pgt.getIsPgtMCom()+pgt.getIsPgtMRare()+pgt.getIsPgtSr()>0){
                    if(pgt.getIsPgtMEbt()+pgt.getIsPgtMCom()+pgt.getIsPgtMRare()+pgt.getIsPgtSr()>0 && pgt.getCreatedAt().before(new Date())){
                        countNo+=pgt.getIsPgtCoFunding();
                    }
                }

            }
        }
        ParamUtil.setSessionAttr(request, "count",countNo);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Preimplantation Genetic Testing</strong>");

        List<SelectOption> embryosBiopsiedLocalSelectOption  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));
        ParamUtil.setRequestAttr(bpc.request,"embryosBiopsiedLocalSelectOption",embryosBiopsiedLocalSelectOption);

        List<SelectOption> biopsyLocalSelectOption= IaisCommonUtils.genNewArrayList();
        biopsyLocalSelectOption.add(new SelectOption("embryologist","embryologist"));
        ParamUtil.setRequestAttr(bpc.request,"biopsyLocalSelectOption",biopsyLocalSelectOption);
    }



    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PgtStageDto pgtStageDto=arSuperDataSubmissionDto.getPgtStageDto();
        pgtStageDto.setIsPgtMCom(0);
        pgtStageDto.setIsPgtMRare(0);
        pgtStageDto.setIsPgtMEbt(0);
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
        String isPgtMCom =  ParamUtil.getString(request, "isPgtMCom");
        String isPgtMRare =  ParamUtil.getString(request, "isPgtMRare");
        String isPgtMEbt =  ParamUtil.getString(request, "isPgtMEbt");
        String isPgtSr =  ParamUtil.getString(request, "isPgtSr");
        String isPgtA =  ParamUtil.getString(request, "isPgtA");
        String isPtt =  ParamUtil.getString(request, "isPtt");
        String isOtherPgt =  ParamUtil.getString(request, "isOtherPgt");
        if("on".equals(isPgtMCom)){
            pgtStageDto.setIsPgtMCom(1);
        }
        if("on".equals(isPgtMRare)){
            pgtStageDto.setIsPgtMRare(1);
        }
        if("on".equals(isPgtMEbt)){
            pgtStageDto.setIsPgtMEbt(1);
        }

        if( "on".equals(isPgtMCom)||"on".equals(isPgtMRare)||"on".equals(isPgtMEbt)){

            String isPgtMDsld =  ParamUtil.getString(request, "isPgtMDsld");
            if("on".equals(isPgtMDsld)){
                pgtStageDto.setIsPgtMDsld(1);
                String pgtMRefNo = ParamUtil.getString(request, "pgtMRefNo");
                pgtStageDto.setPgtMRefNo(pgtMRefNo);
            }

            String isPgtMWithHla =  ParamUtil.getString(request, "isPgtMWithHla");
            if("on".equals(isPgtMWithHla)){
                pgtStageDto.setIsPgtMWithHla(1);
            }
            String isPgtMNon =  ParamUtil.getString(request, "isPgtMNon");
            if("on".equals(isPgtMNon)){
                pgtStageDto.setIsPgtMNon(1);
            }

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
            String otherPgt = ParamUtil.getString(request, "otherPgt");
            pgtStageDto.setOtherPgt(otherPgt);
        }

        String isPgtCoFunding = ParamUtil.getString(request, "isPgtCoFunding");
        if("0".equals(isPgtCoFunding)){
            pgtStageDto.setIsPgtCoFunding(0);
        }
        if("1".equals(isPgtCoFunding)){
            pgtStageDto.setIsPgtCoFunding(1);
        }
        String isEmbryosBiopsiedLocal = ParamUtil.getString(request, "isEmbryosBiopsiedLocal");
        pgtStageDto.setIsEmbryosBiopsiedLocal(isEmbryosBiopsiedLocal);
        if("Others".equals(isEmbryosBiopsiedLocal)){
            String otherEmbryosBiopsiedAddr = ParamUtil.getString(request, "otherEmbryosBiopsiedAddr");
            pgtStageDto.setOtherEmbryosBiopsiedAddr(otherEmbryosBiopsiedAddr);
        }

        String isBiopsyLocal = ParamUtil.getString(request, "isBiopsyLocal");
        pgtStageDto.setIsBiopsyLocal(isBiopsyLocal);

        if("Others".equals(isBiopsyLocal)){
            String otherBiopsyAddr = ParamUtil.getString(request, "otherBiopsyAddr");
            pgtStageDto.setOtherBiopsyAddr(otherBiopsyAddr);
        }
        int count = (int) ParamUtil.getSessionAttr(request,"count");
        if(count>=6){
            String isThereAppeal = ParamUtil.getString(request, "isThereAppeal");
            if("0".equals(isThereAppeal)){
                pgtStageDto.setIsThereAppeal(0);
            }
            if("1".equals(isThereAppeal)){
                pgtStageDto.setIsThereAppeal(1);
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(pgtStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(bpc.request,pgtStageDto);
        }



    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String hciCode=arSuperDataSubmissionDto.getPgtStageDto().getIsEmbryosBiopsiedLocal();
        String value=arSuperDataSubmissionDto.getPgtStageDto().getIsEmbryosBiopsiedLocal();
        List<SelectOption> embryosBiopsiedLocalSelectOption  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));
        for (SelectOption so:embryosBiopsiedLocalSelectOption) {
            if(so.getValue().equals(hciCode)){ value=so.getText();break;}
        }
        arSuperDataSubmissionDto.getPgtStageDto().setEmbryosBiopsiedLocal(value);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);

    }

    protected void valRFC(HttpServletRequest request, PgtStageDto cycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getPgtStageDto()!= null && cycleStageDto.equals(arOldSuperDataSubmissionDto.getPgtStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
