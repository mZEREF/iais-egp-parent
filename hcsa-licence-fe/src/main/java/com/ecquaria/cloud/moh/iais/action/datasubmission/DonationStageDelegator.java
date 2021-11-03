package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
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

        List<SelectOption> curCenDonatedSelectOption= IaisCommonUtils.genNewArrayList();

        ParamUtil.setRequestAttr(bpc.request,"curCenDonatedSelectOption",curCenDonatedSelectOption);
        List<SelectOption> insSentToCurSelectOption= IaisCommonUtils.genNewArrayList();

        ParamUtil.setRequestAttr(bpc.request,"insSentToCurSelectOption",insSentToCurSelectOption);
    }




    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        HttpServletRequest request=bpc.request;
        Integer curCenDonatedNum =  null;
        try {
            curCenDonatedNum =  ParamUtil.getInt(request, "curCenDonatedNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer otherCenDonatedNum = null;
        try {
            otherCenDonatedNum = ParamUtil.getInt(request, "otherCenDonatedNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer resDonarNum = null;
        try {
            resDonarNum =  ParamUtil.getInt(request, "resDonarNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer curCenResDonatedNum = null;
        try {
            curCenResDonatedNum =  ParamUtil.getInt(request, "curCenResDonatedNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer otherCenResDonarNum = null;
        try {
            otherCenResDonarNum =  ParamUtil.getInt(request, "otherCenResDonarNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer trainingNum = null;
        try {
            trainingNum =  ParamUtil.getInt(request, "trainingNum");
        }catch (Exception e){
            log.error("no int");
        }
        int totalNum =0;

        if(curCenDonatedNum!=null){
            totalNum+=curCenDonatedNum;
        }
        if(otherCenDonatedNum!=null){
            totalNum+=otherCenDonatedNum;
        }
        if(resDonarNum!=null){
            totalNum+=resDonarNum;
        }
        if(curCenResDonatedNum!=null){
            totalNum+=curCenResDonatedNum;
        }
        if(otherCenResDonarNum!=null){
            totalNum+=otherCenResDonarNum;
        }
        if(trainingNum!=null){
            totalNum+=trainingNum;
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
        //ID must be a registered patient in the receiving AR Centre
        String directedDonorId=ParamUtil.getString(request,"directedDonorId");
        donationStageDto.setDirectedDonorId(directedDonorId);
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
        if("DONRES004".equals(donationReason)){
            String otherDonationReason=ParamUtil.getString(request,"otherDonationReason");
            donationStageDto.setOtherDonationReason(otherDonationReason);
        }

        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

        ValidationResult validationResult = WebValidationHelper.validateProperty(donationStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();

        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");
    }


}
