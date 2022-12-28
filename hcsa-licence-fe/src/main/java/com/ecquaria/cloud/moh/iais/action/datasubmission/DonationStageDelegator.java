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
            DonationStageDto donationStageDto = new DonationStageDto();
            donationStageDto.setDonatedCentre(arSuperDataSubmissionDto.getPremisesDto().getId());
            donationStageDto.setDonatedCentreAddress(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
            arSuperDataSubmissionDto.setDonationStageDto(donationStageDto);
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
    }




    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);


        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        HttpServletRequest request=bpc.request;
        String localOrOversea = ParamUtil.getString(request,"localOrOversea");
        if (localOrOversea != null) {
            donationStageDto.setLocalOrOversea(Integer.parseInt(localOrOversea));
        }
        String donatedType=ParamUtil.getString(request,"donatedType");
        donationStageDto.setDonatedType(donatedType);
        if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(donationStageDto.getDonatedType()) || DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(donationStageDto.getDonatedType())) {
            doFemaleDonor(request,donationStageDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donationStageDto.getDonatedType())){
            doMaleDonor(request,donationStageDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donationStageDto.getDonatedType())){
            doFemaleDonor(request,donationStageDto);
            doMaleDonor(request,donationStageDto);
        }

        if ("0".equals(localOrOversea)) {
            String overseaDonatedCentre=ParamUtil.getString(request,"overseaDonatedCentre");
            donationStageDto.setOverseaDonatedCentre(overseaDonatedCentre);
        }

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
            String trainingNumStr = ParamUtil.getString(request, "trainingNum");
            donationStageDto.setTrainingNumStr(trainingNumStr);
            if (StringUtil.isNumber(trainingNumStr)) {
                int trainingNum = Integer.parseInt(trainingNumStr);
                donationStageDto.setTrainingNum(trainingNum);
                totalNum+=trainingNum;
            }
        }
        String donatedForTreatment=ParamUtil.getString(request,"donatedForTreatment");
        if("on".equals(donatedForTreatment)){
            donationStageDto.setDonatedForTreatment(1);
            Integer treatNum = null;
            String isDirectedDonationStr = ParamUtil.getString(request,"directedDonation");
            String recipientNo = ParamUtil.getString(request,"recipientNo");
            if (StringUtil.isNotEmpty(isDirectedDonationStr)) {
                donationStageDto.setIsDirectedDonation(Integer.parseInt(isDirectedDonationStr));
            }
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



        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(donationStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }else
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

    private void doFemaleDonor(HttpServletRequest request, DonationStageDto donationStageDto) {
        String isOocyteDonorPatientStr = ParamUtil.getString(request, "isOocyteDonorPatient");
        String isFemaleIdentityKnownStr = ParamUtil.getString(request,"isFemaleIdentityKnown");
        String femaleIdTypeStr = ParamUtil.getString(request,"femaleIdType");
        String femaleIdNumber = ParamUtil.getString(request,"femaleIdNumber");
        String femaleIdNumberPassport = ParamUtil.getString(request,"femaleIdNumberPassport");
        String femaleDonorSampleCode = ParamUtil.getString(request,"femaleDonorSampleCode");
        String femaleDonorAgeStr = ParamUtil.getString(request,"femaleDonorAge");
        if (isOocyteDonorPatientStr != null) {
            donationStageDto.setIsOocyteDonorPatient(Integer.parseInt(isOocyteDonorPatientStr));
        } else {
            donationStageDto.setIsOocyteDonorPatient(null);
        }
        if (isFemaleIdentityKnownStr != null) {
            donationStageDto.setIsFemaleIdentityKnown(Integer.parseInt(isFemaleIdentityKnownStr));
        } else {
            donationStageDto.setIsFemaleIdentityKnown(null);
        }
        if (femaleIdTypeStr != null){
            donationStageDto.setFemaleIdType(Integer.parseInt(femaleIdTypeStr));
        } else {
            donationStageDto.setFemaleIdType(null);
        }
        if ("1".equals(femaleIdTypeStr)){
            donationStageDto.setFemaleIdNumber(femaleIdNumber);
        } else if ("0".equals(femaleIdTypeStr)) {
            donationStageDto.setFemaleIdNumber(femaleIdNumberPassport);
        } else {
            donationStageDto.setFemaleIdNumber(null);
        }
        donationStageDto.setFemaleDonorSampleCode(femaleDonorSampleCode);
        donationStageDto.setFemaleDonorAgeStr(femaleDonorAgeStr);
        if(femaleDonorAgeStr != null && StringUtil.isNumber(femaleDonorAgeStr)) {
            donationStageDto.setFemaleDonorAge(Integer.parseInt(femaleDonorAgeStr));
            donationStageDto.setFemaleDonorAgeStr(null);
        }
    }

    private void doMaleDonor (HttpServletRequest request, DonationStageDto donationStageDto) {
        String isSpermDonorPatientStr = ParamUtil.getString(request, "isSpermDonorPatient");
        String isMaleIdentityKnownStr = ParamUtil.getString(request,"isMaleIdentityKnown");
        String maleIdTypeStr = ParamUtil.getString(request,"maleIdType");
        String maleIdNumber = ParamUtil.getString(request,"maleIdNumber");
        String maleIdNumberPassport = ParamUtil.getString(request,"maleIdNumberPassport");
        String maleDonorSampleCode = ParamUtil.getString(request,"maleDonorSampleCode");
        String maleDonorAgeStr = ParamUtil.getString(request,"maleDonorAge");
        if (isSpermDonorPatientStr != null) {
            donationStageDto.setIsSpermDonorPatient(Integer.parseInt(isSpermDonorPatientStr));
        } else {
            donationStageDto.setIsSpermDonorPatient(null);
        }
        if (isMaleIdentityKnownStr != null) {
            donationStageDto.setIsMaleIdentityKnown(Integer.parseInt(isMaleIdentityKnownStr));
        } else {
            donationStageDto.setIsMaleIdentityKnown(null);
        }
        if (maleIdTypeStr != null){
            donationStageDto.setMaleIdType(Integer.parseInt(maleIdTypeStr));
        } else {
            donationStageDto.setMaleIdType(null);
        }
        if ("1".equals(maleIdTypeStr)){
            donationStageDto.setMaleIdNumber(maleIdNumber);
        } else if ("0".equals(maleIdTypeStr)) {
            donationStageDto.setMaleIdNumber(maleIdNumberPassport);
        } else {
            donationStageDto.setMaleIdNumber(null);
        }
        donationStageDto.setMaleDonorSampleCode(maleDonorSampleCode);
        donationStageDto.setMaleDonorAgeStr(maleDonorAgeStr);
        if(maleDonorAgeStr != null && StringUtil.isNumber(maleDonorAgeStr)) {
            donationStageDto.setMaleDonorAge(Integer.parseInt(maleDonorAgeStr));
            donationStageDto.setMaleDonorAgeStr(null);
        }
    }
}
