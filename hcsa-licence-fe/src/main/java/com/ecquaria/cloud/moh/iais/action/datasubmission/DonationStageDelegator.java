package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
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
    private LicenceClient licenceClient;

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
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");

        List<SelectOption> donatedTypeSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DONATED_TYPE);
        ParamUtil.setRequestAttr(bpc.request,"donatedTypeSelectOption",donatedTypeSelectOption);
        List<SelectOption> donationReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DONATION_REASON);
        ParamUtil.setRequestAttr(bpc.request,"donationReasonSelectOption",donationReasonSelectOption);

        List<SelectOption> selectOptions  = DataSubmissionHelper.genPremisesOptions((Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(bpc.request,DataSubmissionConstant.AR_PREMISES_MAP));

        ParamUtil.setRequestAttr(bpc.request,"curCenDonatedSelectOption",selectOptions);
        ParamUtil.setRequestAttr(bpc.request,"insSentToCurSelectOption",selectOptions);
    }




    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        HttpServletRequest request=bpc.request;
        int totalNum =0;
        Integer curCenDonatedNum =  null;
        try {
            curCenDonatedNum =  ParamUtil.getInt(request, "curCenDonatedNum");
            totalNum+=curCenDonatedNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer otherCenDonatedNum = null;
        try {
            otherCenDonatedNum = ParamUtil.getInt(request, "otherCenDonatedNum");
            totalNum+=otherCenDonatedNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer resDonarNum = null;
        try {
            resDonarNum =  ParamUtil.getInt(request, "resDonarNum");
            totalNum+=resDonarNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer curCenResDonatedNum = null;
        try {
            curCenResDonatedNum =  ParamUtil.getInt(request, "curCenResDonatedNum");
            totalNum+=curCenResDonatedNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer otherCenResDonarNum = null;
        try {
            otherCenResDonarNum =  ParamUtil.getInt(request, "otherCenResDonarNum");
            totalNum+=otherCenResDonarNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer trainingNum = null;
        try {
            trainingNum =  ParamUtil.getInt(request, "trainingNum");
            totalNum+=trainingNum;
        }catch (Exception e){
            log.error("no int");
        }
        donationStageDto.setCurCenDonatedNum(curCenDonatedNum);
        donationStageDto.setOtherCenDonatedNum(otherCenDonatedNum);
        donationStageDto.setResDonarNum(resDonarNum);
        donationStageDto.setCurCenResDonatedNum(curCenResDonatedNum);
        donationStageDto.setOtherCenResDonarNum(otherCenResDonarNum);
        donationStageDto.setTrainingNum(trainingNum);
        donationStageDto.setTotalNum(totalNum);
        donationStageDto.setIsCurCenResTypeHescr(0);
        donationStageDto.setIsCurCenResTypeRrar(0);
        donationStageDto.setIsCurCenResTypeOther(0);
        String donatedType=ParamUtil.getString(request,"donatedType");
        donationStageDto.setDonatedType(donatedType);
        String isCurCenDonated=ParamUtil.getString(request,"isCurCenDonated");
        donationStageDto.setIsCurCenDonated(isCurCenDonated);
        if("Others".equals(isCurCenDonated)){
            String otherDonatedCen=ParamUtil.getString(request,"otherDonatedCen");
            donationStageDto.setOtherDonatedCen(otherDonatedCen);
        }
        if(StringUtil.isNotEmpty(isCurCenDonated)&&!"Others".equals(isCurCenDonated)){
            String directedDonorId=ParamUtil.getString(request,"directedDonorId");
            donationStageDto.setDirectedDonorId(directedDonorId);
        }
        String isCurCenResTypeHescr=ParamUtil.getString(request,"isCurCenResTypeHescr");
        if("on".equals(isCurCenResTypeHescr)){
            donationStageDto.setIsCurCenResTypeHescr(1);
        }


        String isCurCenResTypeRrar=ParamUtil.getString(request,"isCurCenResTypeRrar");
        if("on".equals(isCurCenResTypeRrar)){
            donationStageDto.setIsCurCenResTypeRrar(1);
        }
        String isCurCenResTypeOther=ParamUtil.getString(request,"isCurCenResTypeOther");
        if("on".equals(isCurCenResTypeOther)){
            donationStageDto.setIsCurCenResTypeOther(1);
            String curCenOtherResType=ParamUtil.getString(request,"curCenOtherResType");
            donationStageDto.setCurCenOtherResType(curCenOtherResType);

        }
        String isInsSentToCur=ParamUtil.getString(request,"isInsSentToCur");
        donationStageDto.setIsInsSentToCur(isInsSentToCur);
        if("Others".equals(isInsSentToCur)){
            String insSentToOtherCen=ParamUtil.getString(request,"insSentToOtherCen");
            donationStageDto.setInsSentToOtherCen(insSentToOtherCen);

        }
        String donationReason=ParamUtil.getString(request,"donationReason");
        donationStageDto.setDonationReason(donationReason);
        if(DataSubmissionConsts.DONATION_REASON_OTHERS.equals(donationReason)){
            String otherDonationReason=ParamUtil.getString(request,"otherDonationReason");
            donationStageDto.setOtherDonationReason(otherDonationReason);
        }

        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(donationStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            if(StringUtil.isNotEmpty(donationStageDto.getDirectedDonorId())){
                //ID must be a registered patient in the receiving AR Centre
                donationStageDto.getIsCurCenDonated();
            }
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
        PatientInventoryDto patientInventoryDto = new PatientInventoryDto();
        if(arSuperDataSubmissionDto.getPatientInventoryDto()!=null){
            patientInventoryDto=arSuperDataSubmissionDto.getPatientInventoryDto();
        }
        switch (donationStageDto.getDonatedType()){
            case DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE:
                patientInventoryDto.setChangeFrozenOocytes(donationStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE:
                patientInventoryDto.setChangeFrozenOocytes(donationStageDto.getTotalNum());
                break;

            case DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO:
                patientInventoryDto.setChangeFrozenEmbryos(donationStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM:
                patientInventoryDto.setChangeFrozenSperms(donationStageDto.getTotalNum());
                break;
            default:
        }
        ParamUtil.setRequestAttr(bpc.request, "patientInventoryDto", patientInventoryDto);
    }

}
