package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final static String  DONOR_AGE_DONATION_DROP_DOWN    = "donorAgeDonationDropDown";
    private final static String  DONOR_USED_TYPES                = "donorUsedTypes";
    private final static String  DONOR_SOURSE_DROP_DOWN          = "donorSourseDropDown";
    private final static String  DONOR_SOURSE_OTHERS             = "Others";

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
        ParamUtil.setSessionAttr(request, DONOR_AGE_DONATION_DROP_DOWN,(Serializable)DataSubmissionHelper.getNumsSelections(18,50));
        ParamUtil.setSessionAttr(request, DONOR_USED_TYPES,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_DONOR_USED_TYPE));
        ParamUtil.setSessionAttr(request, DONOR_SOURSE_DROP_DOWN,(Serializable) getSourseList());
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
    private List<SelectOption> getSourseList(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("sourseTest","sourseTest"));
        selectOptions.add(new SelectOption( DONOR_SOURSE_OTHERS,DONOR_SOURSE_OTHERS));
        return selectOptions;
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

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
            arDonorDtos.add(getInitArDonorDto(0));
            arSuperDataSubmissionDto.setArDonorDtos(arDonorDtos);
        }
        arCycleStageDto.setArDonorDtos(arDonorDtos);
        setCycleAgeByPatientInfoDtoAndHcicode(arCycleStageDto,arSuperDataSubmissionDto.getPatientInfoDto(),arSuperDataSubmissionDto.getAppGrpPremisesDto().getHciCode());
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
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
                arCycleStageDto.setCycleAge(IaisCommonUtils.getRecommendationYears(year *12 + month));
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
            //todo valiate ar centre
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
        arCycleStageDto.setCurrentARTreatment(ParamUtil.getStringsToString(request,"currentArTreatment"));
        arCycleStageDto.setCurrentARTreatmentValues(ParamUtil.getListStrings(request,"currentArTreatment"));
        arCycleStageDto.setOtherIndication(ParamUtil.getStringsToString(request,"otherIndication"));
        arCycleStageDto.setOtherIndicationValues(ParamUtil.getListStrings(request,"otherIndication"));
        List<ArDonorDto> arDonorDtos = arCycleStageDto.getArDonorDtos();
        arDonorDtos.forEach(arDonorDto -> {
            String arDonorIndex = String.valueOf(arDonorDto.getArDonorIndex());
            ControllerHelper.get(request,arDonorDto,arDonorIndex);
            arDonorDto.setPleaseIndicate(ParamUtil.getStringsToString(request,"pleaseIndicate"+arDonorIndex));
            arDonorDto.setPleaseIndicateValues(ParamUtil.getListStrings(request,"pleaseIndicate"+arDonorIndex));
            clearNoClearDataForDrDonorDto(arDonorDto);
        });
    }

     private void clearNoClearDataForDrDonorDto(ArDonorDto arDonorDto){
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setDonorSampleCodeId(null);
            arDonorDto.setDonorSampleCode(null);
            arDonorDto.setSource(null);
            arDonorDto.setOtherSource(null);
        }else {
            arDonorDto.setPleaseIndicate(null);
            arDonorDto.setIdNumber(null);
            arDonorDto.setIdType(null);
            arDonorDto.setPleaseIndicateValues(null);
        }
     }
}
