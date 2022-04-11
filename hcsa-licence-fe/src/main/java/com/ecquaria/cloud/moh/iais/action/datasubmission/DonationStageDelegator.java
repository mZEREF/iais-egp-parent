package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DonationStageDelegator
 *
 * @author junyu
 * @date 2021/11/1
 */
@Delegator("donationDelegator")
@Slf4j
public class DonationStageDelegator extends CommonDelegator{
    @Autowired
    DsLicenceService dsLicenceService;
    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Donation");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getDonationStageDto()==null){
            arSuperDataSubmissionDto.setDonationStageDto(new DonationStageDto());
        }

        ParamUtil.setSessionAttr(bpc.request,DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Donation</strong>");

        List<SelectOption> donatedTypeSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DONATED_TYPE);
        ParamUtil.setRequestAttr(bpc.request,"donatedTypeSelectOption",donatedTypeSelectOption);
        List<SelectOption> donationReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DONATION_REASON);
        ParamUtil.setRequestAttr(bpc.request,"donationReasonSelectOption",donationReasonSelectOption);
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremiseList(orgId);
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            premisesSel.add(new SelectOption( premisesDto.getId(), premisesDto.getPremiseLabel()));
        }
        Collections.sort(premisesSel);
        premisesSel.add(0, new SelectOption("", "Please Select"));
        ParamUtil.setRequestAttr(bpc.request,"curCenDonatedSelectOption",premisesSel);
    }




    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);


        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        HttpServletRequest request=bpc.request;
        String donatedType=ParamUtil.getString(request,"donatedType");
        donationStageDto.setDonatedType(donatedType);
        String donatedCentre=ParamUtil.getString(request,"donatedCentre");
        donationStageDto.setDonatedCentre(donatedCentre);
        String donationReason=ParamUtil.getString(request,"donationReason");
        donationStageDto.setDonationReason(donationReason);
        if(DataSubmissionConsts.DONATION_REASON_OTHERS.equals(donationReason)){
            String otherDonationReason=ParamUtil.getString(request,"otherDonationReason");
            donationStageDto.setOtherDonationReason(otherDonationReason);
        }

        int totalNum =0;
        donationStageDto.setDonatedForResearch(0);
        donationStageDto.setDonatedForTraining(0);
        donationStageDto.setDonatedForTreatment(0);
        boolean isInt=true;
        String donatedForResearch=ParamUtil.getString(request,"donatedForResearch");
        if("on".equals(donatedForResearch)){
            donationStageDto.setDonatedForResearch(1);
            Integer donResForTreatNum =  null;
            try {
                String donResForTreatNumString=ParamUtil.getString(request, "donResForTreatNum");
                donationStageDto.setDonResForTreatNumStr(donResForTreatNumString);
                if(StringUtil.isEmpty(donResForTreatNumString)){
                    donationStageDto.setDonResForTreatNum(null);
                }else {
                    donResForTreatNum =  ParamUtil.getInt(request, "donResForTreatNum");
                    donationStageDto.setDonResForTreatNum(donResForTreatNum);
                    totalNum+=donResForTreatNum;
                }

            }catch (Exception e){
                log.error("no int");
                        isInt=false;
                donationStageDto.setDonResForTreatNum(null);
            }
            Integer donResForCurCenNotTreatNum =  null;
            try {
                String donResForCurCenNotTreatNumString=ParamUtil.getString(request, "donResForCurCenNotTreatNum");
                donationStageDto.setDonResForCurCenNotTreatNumStr(donResForCurCenNotTreatNumString);
                if(StringUtil.isEmpty(donResForCurCenNotTreatNumString)){
                    donationStageDto.setDonResForCurCenNotTreatNum(null);
                }else {
                    donResForCurCenNotTreatNum =  ParamUtil.getInt(request, "donResForCurCenNotTreatNum");
                    donationStageDto.setDonResForCurCenNotTreatNum(donResForCurCenNotTreatNum);
                    totalNum+=donResForCurCenNotTreatNum;
                }

            }catch (Exception e){
                log.error("no int");
                        isInt=false;
                donationStageDto.setDonResForCurCenNotTreatNum(null);
            }
            donationStageDto.setDonatedForResearchHescr(0);
            donationStageDto.setDonatedForResearchRrar(0);
            donationStageDto.setDonatedForResearchOther(0);

            String donatedForResearchHescr=ParamUtil.getString(request,"donatedForResearchHescr");
            if("on".equals(donatedForResearchHescr)){
                donationStageDto.setDonatedForResearchHescr(1);
            }
            String donatedForResearchRrar=ParamUtil.getString(request,"donatedForResearchRrar");
            if("on".equals(donatedForResearchRrar)){
                donationStageDto.setDonatedForResearchRrar(1);
            }

            String donatedForResearchOther=ParamUtil.getString(request,"donatedForResearchOther");
            if("on".equals(donatedForResearchOther)){
                donationStageDto.setDonatedForResearchOther(1);
                String donatedForResearchOtherType=ParamUtil.getString(request,"donatedForResearchOtherType");
                donationStageDto.setDonatedForResearchOtherType(donatedForResearchOtherType);
            }

        }
        String donatedForTraining=ParamUtil.getString(request,"donatedForTraining");
        if("on".equals(donatedForTraining)){
            donationStageDto.setDonatedForTraining(1);
            Integer trainingNum = null;
            try {
                String trainingNumString=ParamUtil.getString(request, "trainingNum");
                donationStageDto.setTrainingNumStr(trainingNumString);
                if(StringUtil.isEmpty(trainingNumString)){
                    donationStageDto.setTrainingNum(null);
                }else {
                    trainingNum = ParamUtil.getInt(request, "trainingNum");
                    donationStageDto.setTrainingNum(trainingNum);
                    totalNum+=trainingNum;
                }
            }catch (Exception e){
                log.error("no int");
                        isInt=false;
                donationStageDto.setTrainingNum(null);
            }
        }
        String donatedForTreatment=ParamUtil.getString(request,"donatedForTreatment");
        if("on".equals(donatedForTreatment)){
            donationStageDto.setDonatedForTreatment(1);
            Integer treatNum = null;
            try {
                String treatNumString=ParamUtil.getString(request, "treatNum");
                donationStageDto.setTreatNumStr(treatNumString);
                if(StringUtil.isEmpty(treatNumString)){
                    donationStageDto.setTreatNum(null);
                }else {
                    treatNum =  ParamUtil.getInt(request, "treatNum");
                    donationStageDto.setTreatNum(treatNum);
                    totalNum+=treatNum;
                }
            }catch (Exception e){
                log.error("no int");
                isInt=false;
                donationStageDto.setTreatNum(null);
            }
        }
        if(isInt){
            donationStageDto.setTotalNum(totalNum);
        }else {
            donationStageDto.setTotalNum(null);
        }

        String donatedRecipientNum=ParamUtil.getString(request,"donatedRecipientNum");
        donationStageDto.setDonatedRecipientNum(donatedRecipientNum);

        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(donationStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(bpc.request,donationStageDto);
        }
    }
    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();

        switch (donationStageDto.getDonatedType()){
            case DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE:
                arChangeInventoryDto.setFreshOocyteNum(-donationStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE:
                arChangeInventoryDto.setFrozenOocyteNum(-donationStageDto.getTotalNum());
                break;

            case DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO:
                arChangeInventoryDto.setFrozenEmbryoNum(-donationStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM:
                arChangeInventoryDto.setFrozenSpermNum(-donationStageDto.getTotalNum());
                break;
            default:
        }
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremiseList(orgId);
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            premisesSel.add(new SelectOption( premisesDto.getId(), premisesDto.getPremiseLabel()));
        }
        String arCenter=donationStageDto.getDonatedCentre();
        String value=donationStageDto.getDonatedCentre();
        for (SelectOption so:premisesSel) {
            if(so.getValue().equals(arCenter)){
                value=so.getText();break;
            }
        }
        donationStageDto.setDonatedCentreAddress(value);
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);

    }

    protected void valRFC(HttpServletRequest request, DonationStageDto cycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getDonationStageDto()!= null && cycleStageDto.equals(arOldSuperDataSubmissionDto.getDonationStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
