package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    private ArFeClient arFeClient;
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

        List<SelectOption> selectOptions  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));

        ParamUtil.setRequestAttr(bpc.request,"curCenDonatedSelectOption",selectOptions);
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

        String donatedForResearch=ParamUtil.getString(request,"donatedForResearch");
        if("on".equals(donatedForResearch)){
            donationStageDto.setDonatedForResearch(1);
            Integer donResForTreatNum =  null;
            try {
                String donResForTreatNumString=ParamUtil.getString(request, "donResForTreatNum");
                donationStageDto.setDonResForTreatNumStr(donResForTreatNumString);
                donResForTreatNum =  ParamUtil.getInt(request, "donResForTreatNum");
                donationStageDto.setDonResForTreatNum(donResForTreatNum);
                totalNum+=donResForTreatNum;
            }catch (Exception e){
                log.error("no int");
            }
            Integer donResForCurCenNotTreatNum =  null;
            try {
                String donResForCurCenNotTreatNumString=ParamUtil.getString(request, "donResForCurCenNotTreatNum");
                donationStageDto.setDonResForCurCenNotTreatNumStr(donResForCurCenNotTreatNumString);
                donResForCurCenNotTreatNum =  ParamUtil.getInt(request, "donResForCurCenNotTreatNum");
                donationStageDto.setDonResForCurCenNotTreatNum(donResForCurCenNotTreatNum);
                totalNum+=donResForCurCenNotTreatNum;
            }catch (Exception e){
                log.error("no int");
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
                trainingNum = ParamUtil.getInt(request, "trainingNum");
                donationStageDto.setTrainingNum(trainingNum);
                totalNum+=trainingNum;
            }catch (Exception e){
                log.error("no int");
            }
            Integer treatNum = null;
            try {
                String treatNumString=ParamUtil.getString(request, "treatNum");
                donationStageDto.setTreatNumStr(treatNumString);
                treatNum =  ParamUtil.getInt(request, "treatNum");
                donationStageDto.setTreatNum(treatNum);
                totalNum+=treatNum;
            }catch (Exception e){
                log.error("no int");
            }
        }
        String donatedForTreatment=ParamUtil.getString(request,"donatedForTreatment");
        if("on".equals(donatedForTreatment)){
            donationStageDto.setDonatedForTreatment(1);
        }

        donationStageDto.setTotalNum(totalNum);

        String donatedRecipientNum=ParamUtil.getString(request,"donatedRecipientNum");
        donationStageDto.setDonatedRecipientNum(donatedRecipientNum);

        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(donationStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
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

        List<SelectOption> selectOptions  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));
        String hciCode=donationStageDto.getDonatedCentre();
        String value=donationStageDto.getDonatedCentre();
        for (SelectOption so:selectOptions) {
            if(so.getValue().equals(hciCode)){
                value=so.getText();break;
            }
        }
        donationStageDto.setDonatedCentreAddress(value);
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);

    }

}
