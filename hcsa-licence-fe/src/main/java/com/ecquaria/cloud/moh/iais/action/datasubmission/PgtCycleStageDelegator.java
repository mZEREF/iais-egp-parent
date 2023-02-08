package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * PgoCycleStageDelegator
 *
 * @author junyu
 * @date 2021/10/28
 */
@Delegator("preimplantationDelegator")
@Slf4j
public class PgtCycleStageDelegator extends CommonDelegator{
    private static final String SUBMIT_FLAG = "pgtCycleSubmitFlag__attr";

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
            arSuperDataSubmissionDto.getPgtStageDto().setIsThereAppeal(0);
        }
        initPgtCount(arSuperDataSubmissionDto,bpc.request);


        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    public void initPgtCount(ArSuperDataSubmissionDto arSuperDataSubmissionDto,HttpServletRequest request) {
        String patientCode=arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode();

        List<PgtStageDto> oldPgtList=dpFeClient.listPgtStageByPatientCode(patientCode).getEntity();
        int countNo =0;
        if(oldPgtList!=null){
            for (PgtStageDto pgt:oldPgtList
            ) {
                if(pgt.getIsPgtSr()>0 && pgt.getCreatedAt().before(new Date())){
                    if ("Y".equals(pgt.getIsPgtSrCoFunding())) {
                        countNo += 1;
                    }
                }
                if(pgt.getIsPgtMCom()+pgt.getIsPgtMRare()>0 && pgt.getCreatedAt().before(new Date())){
                    if ("Y".equals(pgt.getIsPgtCoFunding())) {
                        countNo += 1;
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
        pgtStageDto.setWorkUpCom(0);
        pgtStageDto.setEbtCom(0);
        pgtStageDto.setWorkUpRare(0);
        pgtStageDto.setEbtRare(0);

        HttpServletRequest request=bpc.request;
        String isPgtMCom =  ParamUtil.getString(request, "isPgtMCom");
        String isPgtMRare =  ParamUtil.getString(request, "isPgtMRare");
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


        if( "on".equals(isPgtMCom)||"on".equals(isPgtMRare)){
            String pgtMDateStr = ParamUtil.getRequestString(request,"pgtMDate");
            if(!StringUtil.isEmpty(pgtMDateStr)) {
                try{
                    Date date = Formatter.parseDate(pgtMDateStr);
                    pgtStageDto.setPgtMDate(date);
                }catch (Exception e) {
                    pgtStageDto.setPgtMDate(null);
                    log.info("PGT Stage invalid pgtMDate");
                }
            }

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

        if ("on".equals(isPgtMCom) && "on".equals(isPgtMRare)){
            String isPgtCoFunding = ParamUtil.getString(request, "isPgtMComCoFunding");
            String isPgtMRareCoFunding = ParamUtil.getString(request, "isPgtMRareCoFunding");
            if ("Y".equals(isPgtCoFunding) || "Y".equals(isPgtMRareCoFunding)){
                String pgtMAppealStr = ParamUtil.getString(request, "pgtMAppeal");
                if (StringUtil.isNotEmpty(pgtMAppealStr)){
                    pgtStageDto.setPgtMAppeal(Integer.parseInt(pgtMAppealStr));
                }
            }
        }

        if ("on".equals(isPgtMCom)) {
            String isPgtCoFunding = ParamUtil.getString(request, "isPgtMComCoFunding");
            if ("N".equals(isPgtCoFunding)) {
                pgtStageDto.setIsPgtCoFunding("N");
            } else if("Y".equals(isPgtCoFunding)) {
                pgtStageDto.setIsPgtCoFunding("Y");
            } else if ("NA".equals(isPgtCoFunding)) {
                pgtStageDto.setIsPgtCoFunding("NA");
            }
            String mComWork = ParamUtil.getString(request, "mComWork");
            String mComEBT = ParamUtil.getString(request, "mComEBT");
            if ("on".equals(mComWork)){
                pgtStageDto.setWorkUpCom(1);
            }
            if ("on".equals(mComEBT)){
                pgtStageDto.setEbtCom(1);
            }
        }
        if ("on".equals(isPgtMRare)) {
            String isPgtMRareCoFunding = ParamUtil.getString(request, "isPgtMRareCoFunding");
            if ("N".equals(isPgtMRareCoFunding)) {
                pgtStageDto.setIsPgtMRareCoFunding("N");
            } else if("Y".equals(isPgtMRareCoFunding)) {
                pgtStageDto.setIsPgtMRareCoFunding("Y");
            } else if("NA".equals(isPgtMRareCoFunding)) {
                pgtStageDto.setIsPgtMRareCoFunding("NA");
            }
            String mRareWork = ParamUtil.getString(request, "mRareWork");
            String mRareEBT = ParamUtil.getString(request, "mRareEBT");
            if ("on".equals(mRareWork)){
                pgtStageDto.setWorkUpRare(1);
            }
            if ("on".equals(mRareEBT)){
                pgtStageDto.setEbtRare(1);
            }
        }

        if("on".equals(isPgtSr)){
            pgtStageDto.setIsPgtSr(1);
            String pgtSrDateStr = ParamUtil.getRequestString(request,"pgtSrDate");
            if(!StringUtil.isEmpty(pgtSrDateStr)) {
                try{
                    Date date = Formatter.parseDate(pgtSrDateStr);
                    pgtStageDto.setPgtSrDate(date);
                }catch (Exception e) {
                    pgtStageDto.setPgtSrDate(null);
                    log.info("PGT Stage invalid pgtSrDate");
                }
            }
            String pgtSrRefNo = ParamUtil.getString(request, "pgtSrRefNo");
            if(!StringUtil.isEmpty(pgtSrRefNo)){
                pgtStageDto.setPgtSrRefNo(pgtSrRefNo);
            }
            String pgtSrCondition = ParamUtil.getString(request, "pgtSrCondition");
            String isPgtSrCoFunding = ParamUtil.getString(request, "isPgtSrCoFunding");
            if("N".equals(isPgtSrCoFunding)){
                pgtStageDto.setIsPgtSrCoFunding("N");
            } else if("Y".equals(isPgtSrCoFunding)) {
                pgtStageDto.setIsPgtSrCoFunding("Y");
                String pgtSrAppealStr = ParamUtil.getString(request, "pgtSrAppeal");
                if (StringUtil.isNotEmpty(pgtSrAppealStr)){
                    pgtStageDto.setPgtSrAppeal(Integer.parseInt(pgtSrAppealStr));
                }
            } else if("NA".equals(isPgtSrCoFunding)) {
                pgtStageDto.setIsPgtSrCoFunding("NA");
            }
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
            String isPgtACoFunding = ParamUtil.getString(request, "isPgtACoFunding");
            pgtStageDto.setPgtAAppeal(null);
            if("N".equals(isPgtACoFunding)){
                pgtStageDto.setIsPgtACoFunding("N");
            } else if("Y".equals(isPgtACoFunding)) {
                pgtStageDto.setIsPgtACoFunding("Y");
                String pgtAAppealStr = ParamUtil.getString(request, "pgtAAppeal");
                if (pgtAAppealStr != null) {
                    pgtStageDto.setPgtAAppeal(Integer.parseInt(pgtAAppealStr));
                }
            } else if("NA".equals(isPgtACoFunding)) {
                pgtStageDto.setIsPgtACoFunding("NA");
            }
        }

        if("on".equals(isPtt)){
            pgtStageDto.setIsPtt(1);
            String pttCondition = ParamUtil.getString(request, "pttCondition");
            pgtStageDto.setPttCondition(pttCondition);
            String isPttCoFunding = ParamUtil.getString(request, "isPttCoFunding");
            if("N".equals(isPttCoFunding)){
                pgtStageDto.setIsPttCoFunding("N");
            } else if("Y".equals(isPttCoFunding)){
                pgtStageDto.setIsPttCoFunding("Y");
                String pgtPttAppealStr = ParamUtil.getString(request, "pgtPttAppeal");
                if (StringUtil.isNotEmpty(pgtPttAppealStr)){
                    pgtStageDto.setPgtPttAppeal(Integer.parseInt(pgtPttAppealStr));
                }
            } else if("NA".equals(isPttCoFunding)){
                pgtStageDto.setIsPttCoFunding("NA");
            }
        }

        if("on".equals(isOtherPgt)){
            pgtStageDto.setIsOtherPgt(1);
            String otherPgt = ParamUtil.getString(request, "otherPgt");
            pgtStageDto.setOtherPgt(otherPgt);
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
        if(count>=6&&(pgtStageDto.getIsPgtMCom()+pgtStageDto.getIsPgtMRare()+pgtStageDto.getIsPgtSr()>0)
                && (pgtStageDto.getIsPgtCoFunding()!=null && "Y".equals(pgtStageDto.getIsPgtCoFunding())
                || (pgtStageDto.getIsPgtMRareCoFunding()!=null && "Y".equals(pgtStageDto.getIsPgtMRareCoFunding()))
                || (pgtStageDto.getIsPgtSrCoFunding()!=null && "Y".equals(pgtStageDto.getIsPgtSrCoFunding())))){
            String isThereAppeal = ParamUtil.getString(request, "isThereAppeal");
            if("0".equals(isThereAppeal)){
                pgtStageDto.setIsThereAppeal(0);
            }
            if("1".equals(isThereAppeal)){
                pgtStageDto.setIsThereAppeal(1);
            }
        }else {
            pgtStageDto.setIsThereAppeal(0);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(pgtStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }else
            valRFC(bpc.request,pgtStageDto);
        }



    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        arSuperDataSubmissionDto.setArChangeInventoryDto(null);
        String hciCode=arSuperDataSubmissionDto.getPgtStageDto().getIsEmbryosBiopsiedLocal();
        String value=arSuperDataSubmissionDto.getPgtStageDto().getIsEmbryosBiopsiedLocal();
        List<SelectOption> embryosBiopsiedLocalSelectOption  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));
        for (SelectOption so:embryosBiopsiedLocalSelectOption) {
            if(so.getValue().equals(hciCode)){ value=so.getText();break;}
        }
        arSuperDataSubmissionDto.getPgtStageDto().setEmbryosBiopsiedLocal(value);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);

    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        super.doSubmission(bpc);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
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
