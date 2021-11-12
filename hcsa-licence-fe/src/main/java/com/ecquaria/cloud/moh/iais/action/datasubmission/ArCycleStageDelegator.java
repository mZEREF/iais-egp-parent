package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArDonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ArCycleStageDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Delegator("arCycleStageDelegator")
@Slf4j
public class ArCycleStageDelegator extends CommonDelegator {

    private final static String  CURRENT_AR_TREATMENT_SESSION    = "currentArTreatments";
    private final static String  NO_CHILDREN_DROP_DOWN           = "noChildrenDropDown";
    private final static String  NUMBER_ARC_PREVIOUSLY_DROP_DOWN = "numberArcPreviouslyDropDown";
    private final static String  CRUD_ACTION_VALUE_AR_STAGE      = "crud_action_value_ar_stage";
    private final static String  CRUD_ACTION_VALUE_VALIATE_DONOR = "crud_action_value_valiate_donor";
    private final static String  PRACTITIONER_DROP_DOWN          = "practitionerDropDown";
    private final static String  EMBRYOLOGIST_DROP_DOWN          = "embryologistDropDown";
    private final static String  DONOR_USED_TYPES                = "donorUsedTypes";
    private final static String  DONOR_SOURSE_DROP_DOWN          = "donorSourseDropDown";
    private final static String  DONOR_SOURSE_OTHERS             = "Others";
    private final static String  DONOR_SAMPLE_DROP_DOWN          = "donorSampleDropDown";
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,CURRENT_AR_TREATMENT_SESSION,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CURRENT_AR_TREATMENT));
        ParamUtil.setSessionAttr(request, NO_CHILDREN_DROP_DOWN,(Serializable) DataSubmissionHelper.getNumsSelections(10));
        //21 : Unknown
        ParamUtil.setSessionAttr(request, NUMBER_ARC_PREVIOUSLY_DROP_DOWN,(Serializable) DataSubmissionHelper.getNumsSelections(21));
        ParamUtil.setSessionAttr(request, PRACTITIONER_DROP_DOWN,(Serializable) getPractitioner());
        ParamUtil.setSessionAttr(request, EMBRYOLOGIST_DROP_DOWN,(Serializable) getEmbryologist());
        ParamUtil.setSessionAttr(request, DONOR_USED_TYPES,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_DONOR_USED_TYPE));
        ParamUtil.setSessionAttr(request, DONOR_SOURSE_DROP_DOWN,(Serializable) getSourseList(request));
        ParamUtil.setSessionAttr(request, DONOR_SAMPLE_DROP_DOWN,(Serializable) getSampleDropDown());
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    //TODO from ar center
    private List<SelectOption> getPractitioner(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("practitioner","practitioner"));
        return selectOptions;
    }
    //TODO from ar center
    private List<SelectOption> getEmbryologist(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("embryologist","embryologist"));
        return selectOptions;
    }
    //TODO from ar center
    private List<SelectOption> getSourseList(HttpServletRequest request){
        List<SelectOption> selectOptions  = DataSubmissionHelper.genPremisesOptions((Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request,DataSubmissionConstant.AR_PREMISES_MAP));
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,DONOR_SOURSE_OTHERS));
        return selectOptions;
    }

    private List<SelectOption> getSampleDropDown(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList(4);
        MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE).stream().forEach(
                obj -> selectOptions.add(new SelectOption(obj.getCode(),obj.getCodeValue()))
        );
        selectOptions.add(new SelectOption("AR_IT_005","Code"));
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
        List<ArDonorDto> arDonorDtos = arSuperDataSubmissionDto.getArDonorDtos();
        if(arCycleStageDto == null){
            arCycleStageDto = new ArCycleStageDto();
            arSuperDataSubmissionDto.setArCycleStageDto(arCycleStageDto);
        }
        if(IaisCommonUtils.isEmpty(arDonorDtos)){
            arDonorDtos = IaisCommonUtils.genNewArrayList();
            arSuperDataSubmissionDto.setArDonorDtos(arDonorDtos);
        }
        arCycleStageDto.setArDonorDtos(arDonorDtos);
        setCycleAgeByPatientInfoDtoAndHcicode(arCycleStageDto,arSuperDataSubmissionDto.getPatientInfoDto(),arSuperDataSubmissionDto.getAppGrpPremisesDto().getHciCode());
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
    }

    private void setCycleAgeByPatientInfoDtoAndHcicode(ArCycleStageDto arCycleStageDto, PatientInfoDto patientInfoDto,String hciCode){
        if(patientInfoDto != null && patientInfoDto.getPatient() !=null){
            PatientDto patientDto = patientInfoDto.getPatient();
            List<Integer> integers = Formatter.getYearsAndDays(patientDto.getBirthDate());
            if(IaisCommonUtils.isNotEmpty(integers)){
                int year = integers.get(0);
                int month = integers.get(integers.size()-1);
                arCycleStageDto.setCycleAgeYear(year);
                arCycleStageDto.setCycleAgeMonth(month);
                arCycleStageDto.setCycleAge(IaisCommonUtils.getYearsAndMonths(year,month));
            }
            List<CycleDto> cycleDtos = arDataSubmissionService.getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(patientDto.getPatientCode(),hciCode, DataSubmissionConsts.AR_CYCLE_AR);
            arCycleStageDto.setNumberOfCyclesUndergoneLocally(IaisCommonUtils.isNotEmpty(cycleDtos) ? cycleDtos.size() : 0);
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        setArCycleStageDtoByPage(request,arCycleStageDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
        validatePageDataHaveValidationProperty(request,arCycleStageDto,"save",arCycleStageDto.getArDonorDtos(),getByArCycleStageDto(arCycleStageDto), ACTION_TYPE_CONFIRM);
        arCycleStageDto.getArDonorDtos().forEach(arDonorDto -> setEmptyDataForNullDrDonorDto(arDonorDto));
        actionArDonorDtos(request,arCycleStageDto.getArDonorDtos());
        valiateDonorDtos(request,arCycleStageDto.getArDonorDtos());
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,request);
    }
    private void actionArDonorDtos(HttpServletRequest request,List<ArDonorDto> arDonorDtos){
        int actionArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_AR_STAGE);
        //actionArDonor default =-3;
        if(actionArDonor == -1){
            //add
            for (int i = 0; i < arDonorDtos.size(); i++) {
                arDonorDtos.get(i).setArDonorIndex(i);
            }
            arDonorDtos.add(getInitArDonorDto(arDonorDtos.size()));
        }else if(actionArDonor >-1 ){
            //delete
            arDonorDtos.remove(actionArDonor);
            for (int i = 0; i < arDonorDtos.size(); i++) {
                arDonorDtos.get(i).setArDonorIndex(i);
            }
        }else if(actionArDonor == -3){
            arDonorDtos.clear();
        }else if(actionArDonor == -4){
            arDonorDtos.clear();
            arDonorDtos.add(getInitArDonorDto(0));
        }
    }

    private ArDonorDto getInitArDonorDto(int arDonorIndex){
        ArDonorDto arDonorDto = new ArDonorDto();
        arDonorDto.setDirectedDonation(true);
        arDonorDto.setArDonorIndex(arDonorIndex);
        return arDonorDto;
    }

    private void valiateDonorDtos(HttpServletRequest request,List<ArDonorDto> arDonorDtos){
        int valiateArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_VALIATE_DONOR);
        if(valiateArDonor >-1){
            ArDonorDto arDonorDto = arDonorDtos.get(valiateArDonor);
            DonorSampleDto donorSampleDto = arDataSubmissionService.getDonorSampleDto(arDonorDto.getIdType(),arDonorDto.getIdNumber(),
                                                           arDonorDto.getDonorSampleCode(),arDonorDto.getSource(),arDonorDto.getOtherSource());
            if(donorSampleDto == null){
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
                errorMap.put("field1","The corresponding donor");
                errorMap.put("field2","the AR centre");
                String dsErr =  MessageUtil.getMessageDesc("DS_ERR012",errorMap).trim();
                errorMap.clear();
                errorMap.put("validateDonor" +(arDonorDto.isDirectedDonation() ? "Yes" : "No") +arDonorDto.getArDonorIndex(),dsErr);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }else {
                arDonorDto.setRelation(donorSampleDto.getDonarRelation());
                List<String> ages =  StringUtil.isEmpty(donorSampleDto.getAge()) ? null : Arrays.asList(donorSampleDto.getAge().split(","));
                if(ages != null){
                    ages.sort(String::compareTo);
                    List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList( ages.size());
                    ages.stream().forEach(
                          obj-> selectOptions.add(new SelectOption(obj,obj))
                    );
                    arDonorDto.setAgeList(selectOptions);
                }
            }
        }
    }


    private Map<Object,ValidationProperty> getByArCycleStageDto(ArCycleStageDto arCycleStageDto){
        Map<Object,ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(arCycleStageDto.getArDonorDtos())){
            arCycleStageDto.getArDonorDtos().forEach( arDonorDto -> {
                validationPropertyList.put(arDonorDto,new ValidationProperty(arDonorDto.getArDonorIndex(),null,String.valueOf(arDonorDto.getArDonorIndex())));
                    }
            );
        }
        return validationPropertyList;
    }

    private void setArCycleStageDtoByPage(HttpServletRequest request,ArCycleStageDto arCycleStageDto){
        ControllerHelper.get(request,arCycleStageDto);
        arCycleStageDto.setCurrentArTreatment(ParamUtil.getStringsToString(request,"currentArTreatment"));
        arCycleStageDto.setCurrentArTreatmentValues(ParamUtil.getListStrings(request,"currentArTreatment"));
        arCycleStageDto.setOtherIndication(ParamUtil.getStringsToString(request,"otherIndication"));
        arCycleStageDto.setOtherIndicationValues(ParamUtil.getListStrings(request,"otherIndication"));
        setArCycleStageByCurrentARTreatmentValues(arCycleStageDto);
        List<ArDonorDto> arDonorDtos = arCycleStageDto.getArDonorDtos();
        arDonorDtos.forEach(arDonorDto -> {
            String arDonorIndex = String.valueOf(arDonorDto.getArDonorIndex());
            ControllerHelper.get(request,arDonorDto,arDonorIndex);
            arDonorDto.setPleaseIndicate(ParamUtil.getStringsToString(request,"pleaseIndicate"+arDonorIndex));
            arDonorDto.setPleaseIndicateValues(ParamUtil.getListStrings(request,"pleaseIndicate"+arDonorIndex));
            clearNoClearDataForDrDonorDto(arDonorDto,request);
            setArDonorDtoByPleaseIndicate(arDonorDto);
        });
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

    private void setArDonorDtoByPleaseIndicate(ArDonorDto arDonorDto){
        if(StringUtil.isNotEmpty(arDonorDto.getPleaseIndicate())){
            String pleaseIndicate = arDonorDto.getPleaseIndicate();
            arDonorDto.setDonorIndicateEmbryo(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_EMBRYO_USED));
            arDonorDto.setDonorIndicateFresh(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FRESH_OOCYTE));
            arDonorDto.setDonorIndicateFrozen(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FROZEN_OOCYTE_USED));
            arDonorDto.setDonorIndicateSperm(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_SPERMS_USED));
        }
    }

     private void clearNoClearDataForDrDonorDto(ArDonorDto arDonorDto,HttpServletRequest request){
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setDonorSampleCode(null);
            arDonorDto.setSource(null);
            arDonorDto.setOtherSource(null);
            arDonorDto.setIdType(StringUtil.getNonNull(arDonorDto.getIdType()));
            arDonorDto.setIdNumber(StringUtil.getNonNull(arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setPleaseIndicate(null);
            arDonorDto.setPleaseIndicateValues(null);
            arDonorDto.setIdType(ParamUtil.getString(request,"idTypeSample"+arDonorDto.getArDonorIndex()));
            arDonorDto.setSource(StringUtil.getNonNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getNonNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getNonNull(arDonorDto.getDonorSampleCode()));
        }
     }

    private void setEmptyDataForNullDrDonorDto(ArDonorDto arDonorDto){
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setIdType(StringUtil.getStringEmptyToNull( arDonorDto.getIdType()));
            arDonorDto.setIdNumber(StringUtil.getStringEmptyToNull( arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setIdType(StringUtil.getStringEmptyToNull(arDonorDto.getIdType()));
            arDonorDto.setSource(StringUtil.getStringEmptyToNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getStringEmptyToNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getStringEmptyToNull(arDonorDto.getDonorSampleCode()));
        }
    }

}
