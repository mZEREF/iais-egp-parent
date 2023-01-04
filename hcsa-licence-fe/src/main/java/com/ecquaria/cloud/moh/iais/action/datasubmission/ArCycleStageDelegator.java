package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ArCycleStageDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Delegator("arCycleStageDelegator")
@Slf4j
public class ArCycleStageDelegator extends DonorCommonDelegator{
    @Autowired
    private ArFeClient arFeClient;

    private static final String  CURRENT_AR_TREATMENT_SESSION    = "currentArTreatments";
    private static final String  NO_CHILDREN_DROP_DOWN           = "noChildrenDropDown";
    private static final String  NUMBER_ARC_PREVIOUSLY_DROP_DOWN = "numberArcPreviouslyDropDown";
    private static final String  PRACTITIONER_DROP_DOWN          = "practitionerDropDown";
    private static final String  EMBRYOLOGIST_DROP_DOWN          = "embryologistDropDown";

    private static final String  UNDERGONE_OVERSEAS_DROP_DOWN    = "cyclesUndergoneOverseasDropDown";
    private static final String  INIT_IN_ARCYCLE_STAGE           = "INIT_IN_ARCYCLE_STAGE";

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        init(request);
    }

    public void init(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,CURRENT_AR_TREATMENT_SESSION,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CURRENT_AR_TREATMENT));
        ParamUtil.setSessionAttr(request, NO_CHILDREN_DROP_DOWN,(Serializable) DataSubmissionHelper.getNumsSelections(10));
        ParamUtil.setSessionAttr(request, UNDERGONE_OVERSEAS_DROP_DOWN,(Serializable) DataSubmissionHelper.getNumsSelections(10));
        //21 : Unknown
        List<SelectOption> selectOptions =  DataSubmissionHelper.getNumsSelections(20);
        selectOptions.add(new SelectOption("21","Unknown"));
        ParamUtil.setSessionAttr(request, NUMBER_ARC_PREVIOUSLY_DROP_DOWN,(Serializable) selectOptions);
        ParamUtil.setSessionAttr(request, PRACTITIONER_DROP_DOWN,(Serializable) getPractitioner());
        ParamUtil.setSessionAttr(request, EMBRYOLOGIST_DROP_DOWN,(Serializable) getEmbryologist());
        setDonorUserSession(request);
        ParamUtil.setSessionAttr(request, "DSACK002Message","<p>"+MessageUtil.getMessageDesc("DS_ACK002")+"</p>");
        ParamUtil.setRequestAttr(request,INIT_IN_ARCYCLE_STAGE,AppConsts.YES);
        ParamUtil.setRequestAttr(request, "comPareStartAge", DataSubmissionHelper.getCompareStartAge());
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
    }

    //TODO from ar center
    private List<SelectOption> getPractitioner(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("pt01","pt01"));
        return selectOptions;
    }
    //TODO from ar center
    private List<SelectOption> getEmbryologist(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("et01","et01"));
        selectOptions.add(new SelectOption("Not-Applicable","Not-Applicable"));
        return selectOptions;
    }


    @Override
    public void returnStep(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_PAGE);
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        List<DonorDto> arDonorDtos = null;
        if(arCycleStageDto == null){
            arCycleStageDto = new ArCycleStageDto();
            arSuperDataSubmissionDto.setArCycleStageDto(arCycleStageDto);
        }else {
            arDonorDtos = arCycleStageDto.getDonorDtos();
            for (DonorDto arDonorDto: arDonorDtos) {
                if (StringUtil.isNotEmpty(arDonorDto.getOtherSource())) {
                    arDonorDto.setSourceAddress(arDonorDto.getOtherSource());
                } else if(StringUtil.isNotEmpty(arDonorDto.getSource())) {
                    DsCenterDto centerDto = arFeClient.getDsCenterById(arDonorDto.getSource()).getEntity();
                    arDonorDto.setSourceAddress(DsHelper.transfer(centerDto).getPremiseLabel());
                }
            }
        }
        if(IaisCommonUtils.isEmpty(arDonorDtos)){
            arDonorDtos = IaisCommonUtils.genNewArrayList();
        }
        arCycleStageDto.setDonorDtos(arDonorDtos);
        setCycleAgeByPatientInfoDtoAndHcicode(arCycleStageDto,arSuperDataSubmissionDto.getPatientInfoDto(),arSuperDataSubmissionDto.getPremisesDto().getHciCode());
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
        if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,INIT_IN_ARCYCLE_STAGE))){
            setEnhancedCounsellingTipShow(request,arCycleStageDto,false);
            if(isRfc(request)){
                arCycleStageDto.setOldDonorDtos(IaisCommonUtils.isNotEmpty(arDonorDtos) ? (List<DonorDto>) CopyUtil.copyMutableObjectList(arDonorDtos) : null);
            }
            setNumberOfCyclesUndergoneLocally(arCycleStageDto,arDataSubmissionService.getCycleStageSelectionDtoByConds(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode(),null,null));
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
            initOldDonorSelectSession(request,1);
        }
    }

    public void setEnhancedCounsellingTipShow(HttpServletRequest request, ArCycleStageDto arCycleStageDto,boolean needTip){
        if((arCycleStageDto.getCycleAgeYear() > 45 || arCycleStageDto.getCycleAgeYear() == 45 && arCycleStageDto.getCycleAgeMonth() > 0)
                || arCycleStageDto.getCountForEnhancedCounselling() >10){

            if(arCycleStageDto.getEnhancedCounselling() == null || !arCycleStageDto.getEnhancedCounselling()){
                if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,INIT_IN_ARCYCLE_STAGE))){
                    ParamUtil.setRequestAttr(request,"enhancedCounsellingTipShow", AppConsts.YES);
                }
                if(needTip){
                    ParamUtil.setRequestAttr(request, "DS_ERR018Tip","<p>"+MessageUtil.getMessageDesc("DS_ERR018")+"</p>");
                }
            }
        }
    }

    public void setNumberOfCyclesUndergoneLocally(ArCycleStageDto arCycleStageDto,CycleStageSelectionDto selectionDto){
        if(selectionDto != null && IaisCommonUtils.isNotEmpty(selectionDto.getCycleDtos())){
            arCycleStageDto.setNumberOfCyclesUndergoneLocally((int)selectionDto.getCycleDtos().stream().filter(cycleDto -> DataSubmissionConsts.DS_CYCLE_AR.equalsIgnoreCase(cycleDto.getCycleType()) &&
                    !DataSubmissionConsts.DS_STATUS_COMPLETED_END_WITH_ABANDONED.equalsIgnoreCase(cycleDto.getStatus()) && DsHelper.isCycleFinalStatusWithSpec(cycleDto.getStatus())).count());
        }
    }


    public void setCycleAgeByPatientInfoDtoAndHcicode(ArCycleStageDto arCycleStageDto, PatientInfoDto patientInfoDto,String hciCode){
        if(patientInfoDto != null && patientInfoDto.getPatient() !=null){
            PatientDto patientDto = patientInfoDto.getPatient();
            List<CycleDto> cycleDtos = arDataSubmissionService.getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(patientDto.getPatientCode(),hciCode, DataSubmissionConsts.AR_CYCLE_AR);
            arCycleStageDto.setNumberOfCyclesUndergoneLocally(IaisCommonUtils.isNotEmpty(cycleDtos) ? cycleDtos.size() : 0);
            //set ar count for EnhancedCounselling
            arCycleStageDto.setCountForEnhancedCounselling(arDataSubmissionService.getArCycleStageCountByIdTypeAndIdNoAndNationality(patientDto));
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        setArCycleStageDtoByPage(request,arCycleStageDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
        validatePageDataHaveValidationProperty(request,arCycleStageDto,"save",arCycleStageDto.getDonorDtos(),getByArCycleStageDto(arCycleStageDto.getDonorDtos()), ACTION_TYPE_CONFIRM);
        List<DonorDto> donorDtos = arCycleStageDto.getDonorDtos();
        actionArDonorDtos(request,donorDtos);
        valiateDonorDtos(request,donorDtos);
        donorDtos.forEach(arDonorDto -> setEmptyDataForNullDrDonorDto(arDonorDto));
        if(ACTION_TYPE_CONFIRM.equalsIgnoreCase(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE))){
        setEnhancedCounsellingTipShow(request,arCycleStageDto,true);
        checkDonorsVerifyPass(donorDtos,request);
        valRFC(request,arCycleStageDto);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
    }

    protected void valRFC(HttpServletRequest request, ArCycleStageDto arCycleStageDto){
        if(IaisCommonUtils.isEmpty((Map<String, String>) ParamUtil.getRequestAttr(request,IaisEGPConstant.ERRORMAP)) && isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getArCycleStageDto()!= null && arCycleStageDto .equals(arOldSuperDataSubmissionDto.getArCycleStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }



    private void setArCycleStageDtoByPage(HttpServletRequest request,ArCycleStageDto arCycleStageDto){
        ControllerHelper.get(request,arCycleStageDto);
        String yearNumStr = ParamUtil.getRequestString(request,"startYear");
        String monthNumStr = ParamUtil.getRequestString(request,"startMonth");
        if(StringUtil.isNotEmpty(yearNumStr) && StringUtil.isNumber(yearNumStr)) {
            arCycleStageDto.setCycleAgeYear(Integer.parseInt(yearNumStr));
        }
        if(StringUtil.isNotEmpty(monthNumStr) && StringUtil.isNumber(monthNumStr)) {
            arCycleStageDto.setCycleAgeMonth(Integer.parseInt(monthNumStr));
        }
        arCycleStageDto.setCycleAge(IaisCommonUtils.getYearsAndMonths(Integer.parseInt(yearNumStr), Integer.parseInt(monthNumStr)));
        arCycleStageDto.setCurrentArTreatment(ParamUtil.getStringsToString(request,"currentArTreatment"));
        arCycleStageDto.setCurrentArTreatmentValues(ParamUtil.getListStrings(request,"currentArTreatment"));
        arCycleStageDto.setOtherIndication(ParamUtil.getStringsToString(request,"otherIndication"));
        arCycleStageDto.setOtherIndicationValues(ParamUtil.getListStrings(request,"otherIndication"));
        setArCycleStageByCurrentARTreatmentValues(arCycleStageDto);
        setDonorDtos(request,arCycleStageDto.getDonorDtos(),true);
    }

    private void setArCycleStageByCurrentARTreatmentValues(ArCycleStageDto arCycleStageDto){
        if(StringUtil.isNotEmpty(arCycleStageDto.getCurrentArTreatment())){
            String currentARTreatment = arCycleStageDto.getCurrentArTreatment();
            arCycleStageDto.setTreatmentFreshStimulated(currentARTreatment.contains(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_STIMULATED));
            arCycleStageDto.setTreatmentFrozenEmbryo(currentARTreatment.contains(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_EMBRYO_CYCLE));
            arCycleStageDto.setTreatmentFreshNatural(currentARTreatment.contains(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_NATURAL));
            arCycleStageDto.setTreatmentFrozenOocyte(currentARTreatment.contains(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_OOCYTE_CYCLE));
        }
    }





}
