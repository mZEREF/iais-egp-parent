package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.validation.dataSubmission.DonorValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j

public abstract class DonorCommonDelegator extends CommonDelegator{
    private final static String  CRUD_ACTION_VALUE_AR_STAGE      = "crud_action_value_ar_stage";
    private final static String  CRUD_ACTION_VALUE_VALIATE_DONOR = "crud_action_value_valiate_donor";
    protected final static String  DONOR_SAMPLE_DROP_DOWN          = "donorSampleDropDown";
    protected final static String  DONOR_SOURSE_DROP_DOWN          = "donorSourseDropDown";
    private final static String  DONOR_USED_TYPES                = "donorUsedTypes";
    private final static String ADD_DONOR_MAX_SIZE               ="arAddDonorMaxSize";
    private final static String OLD_DONORS_SELECTED               ="oldDonorsSelected";
    protected void setDonorUserSession(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, DONOR_USED_TYPES,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_DONOR_USED_TYPE));
        ParamUtil.setSessionAttr(request, DONOR_SOURSE_DROP_DOWN,(Serializable) getSourseList(request));
        ParamUtil.setSessionAttr(request, DONOR_SAMPLE_DROP_DOWN,(Serializable) getSampleDropDown());
        ParamUtil.setSessionAttr(request, ADD_DONOR_MAX_SIZE,SystemParamUtil.getSystemParamConfig().getArAddDonorMaxSize());
        ParamUtil.clearSession(request,OLD_DONORS_SELECTED);
    }
    protected void actionArDonorDtos(HttpServletRequest request, List<DonorDto> arDonorDtos){
        int actionArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_AR_STAGE);
        //actionArDonor default =-3;
        if(actionArDonor == -1){
            //add
            if(SystemParamUtil.getSystemParamConfig().getArAddDonorMaxSize() > arDonorDtos.size()){
                for (int i = 0; i < arDonorDtos.size(); i++) {
                    arDonorDtos.get(i).setArDonorIndex(i);
                }
                arDonorDtos.add(getInitArDonorDto(arDonorDtos.size()));
            }else {
                log.info("-------user illegal operation addArDonor-----------");
            }
        }else if(actionArDonor >-1 ){
            //delete
            deleteDonor(arDonorDtos,actionArDonor);
        }else if(actionArDonor == -3){
            arDonorDtos.clear();
        }else if(actionArDonor == -4){
            arDonorDtos.clear();
            arDonorDtos.add(getInitArDonorDto(0));
        }
    }

    private void deleteDonor(List<DonorDto> arDonorDtos,int actionArDonor){
        arDonorDtos.remove(actionArDonor);
        for (int i = 0; i < arDonorDtos.size(); i++) {
            arDonorDtos.get(i).setArDonorIndex(i);
        }
    }

    private void setAgeList(DonorDto arDonorDto){
        if(IaisCommonUtils.isNotEmpty(arDonorDto.getAgeList())){
            arDonorDto.getAgeList().clear();
        }
        List<SelectOption> ageList = IaisCommonUtils.genNewArrayList();
        arDonorDto.getDonorSampleAgeDtos().stream().forEach(obj->{
            if(obj.getId().equalsIgnoreCase(arDonorDto.getAge())|| DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE.equalsIgnoreCase(obj.getStatus())){
                ageList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
            }
        });
        arDonorDto.setAgeList(ageList);
    }


    private DonorDto getInitArDonorDto(int arDonorIndex){
        DonorDto arDonorDto = new DonorDto();
        arDonorDto.setDirectedDonation(true);
        arDonorDto.setArDonorIndex(arDonorIndex);
        return arDonorDto;
    }

    protected void valiateDonorDtos(HttpServletRequest request,List<DonorDto> arDonorDtos){
        int valiateArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_VALIATE_DONOR);
        if(valiateArDonor >-1){
            DonorDto arDonorDto = arDonorDtos.get(valiateArDonor);
            Map<String, String> errorMapVal = DonorValidator.valCommonField(IaisCommonUtils.genNewHashMap(2),arDonorDto,true);
            if(IaisCommonUtils.isNotEmpty(errorMapVal)){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMapVal));
                return;
            }
            DonorSampleDto donorSampleDto = arDataSubmissionService.getDonorSampleDto(arDonorDto.isDirectedDonation(),
                    arDonorDto.getIdType(),
                    arDonorDto.getIdNumber(),
                    arDonorDto.getIdType(),
                    arDonorDto.getDonorSampleCode(),
                    DataSubmissionHelper.getLoginContext(request).getLicenseeId(),
                    DataSubmissionHelper.getCurrentArDataSubmission(request).getHciCode());
            if(donorSampleDto == null || IaisCommonUtils.isEmpty(donorSampleDto.getDonorSampleAgeDtos())){
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(2);
                String dsErr;
                if(donorSampleDto == null){
                    errorMap.put("field1","The corresponding donor");
                    errorMap.put("field2","the AR centre");
                    dsErr =  MessageUtil.getMessageDesc("DS_ERR012",errorMap).trim();
                }else {
                    errorMap.put("field","The donor's age(s)");
                    dsErr = MessageUtil.getMessageDesc("DS_ERR020",errorMap).trim();
                }
                errorMap.clear();
                errorMap.put("validateDonor" +(arDonorDto.isDirectedDonation() ? "Yes" : "No") +arDonorDto.getArDonorIndex(),dsErr);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }else {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
                arDonorDtos.forEach( donorDto -> {
                    if(donorSampleDto.getSampleKey().equalsIgnoreCase(donorDto.getDonorSampleKey()) && donorDto.getArDonorIndex() != valiateArDonor){
                            errorMap.put("validateDonor" +(arDonorDto.isDirectedDonation() ? "Yes" : "No") +arDonorDto.getArDonorIndex(), MessageUtil.replaceMessage("DS_ERR016","This donor","field"));
                        setDonorDtoByDonorSampleDto(donorDto,donorSampleDto,request);
                    }
                });
                if(errorMap.isEmpty()){
                    setDonorDtoByDonorSampleDto(arDonorDto,donorSampleDto,request);
                }else {
                    clearDonorAges(arDonorDto);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                }
            }
        }
    }

    private void setDonorDtoByDonorSampleDto(DonorDto arDonorDto, DonorSampleDto donorSampleDto,HttpServletRequest request){
        arDonorDto.setDonorIdentityKnown(donorSampleDto.getDonorIdentityKnown());
        setRfcDonorSelectData(request,donorSampleDto);
        List<DonorSampleAgeDto> ages = donorSampleDto.getDonorSampleAgeDtos();
        arDonorDto.setDonorSampleAgeDtos(ages);
        arDonorDto.setDonorSampleKey(donorSampleDto.getSampleKey());
        arDonorDto.setDonorSampleId(donorSampleDto.getId());
        if(!donorSampleDto.isDirectedDonation()){
            arDonorDto.setSource(donorSampleDto.getSampleFromHciCode());
            arDonorDto.setOtherSource(donorSampleDto.getSampleFromOthers());
        }
        if(IaisCommonUtils.isNotEmpty(ages)){
            arDonorDto.setResetDonor(AppConsts.NO);
            setAgeList(arDonorDto);
        }
    }

    private  void setRfcDonorSelectData(HttpServletRequest request,DonorSampleDto donorSampleDto){
        if(isRfc(request)){
            List<DonorSampleAgeDto> oldDonorSampleAgeDtos =(List<DonorSampleAgeDto>) ParamUtil.getSessionAttr(request,OLD_DONORS_SELECTED);
            if(IaisCommonUtils.isNotEmpty(oldDonorSampleAgeDtos)){
                List<DonorSampleAgeDto> ages = donorSampleDto.getDonorSampleAgeDtos();
                for (DonorSampleAgeDto donorSampleAgeDto : oldDonorSampleAgeDtos){
                    if(donorSampleDto.getSampleKey().equalsIgnoreCase(donorSampleAgeDto.getSampleKey())){
                        boolean isHaveSel = false;
                        for (DonorSampleAgeDto donorSampleAgeDtoF : ages) {
                            if(donorSampleAgeDtoF.getId().equalsIgnoreCase(donorSampleAgeDto.getId())){
                                isHaveSel = true;break;
                            }
                        }
                        if(!isHaveSel){
                            donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                            ages.add(donorSampleAgeDto);
                        }
                    }
                }
                Collections.sort(ages,Comparator.comparingInt(DonorSampleAgeDto::getAge));
            }
        }
    }

    protected void clearNoClearDataForDrDonorDto(DonorDto arDonorDto,HttpServletRequest request){
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setDonorSampleCode(null);
            arDonorDto.setSource(null);
            arDonorDto.setOtherSource(null);
            arDonorDto.setIdType(StringUtil.getNonNull(arDonorDto.getIdType()));
            arDonorDto.setIdNumber(StringUtil.getNonNull(arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setIdType(ParamUtil.getString(request,"idTypeSample"+arDonorDto.getArDonorIndex()));
            arDonorDto.setSource(StringUtil.getNonNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getNonNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getNonNull(arDonorDto.getDonorSampleCode()));
            arDonorDto.setRelation(null);
            arDonorDto.setIdNumber(null);
        }

        if(!AppConsts.NO.equalsIgnoreCase(arDonorDto.getResetDonor())){
            clearDonorAges(arDonorDto);
        }
    }

    private void clearDonorAges(DonorDto arDonorDto){
        arDonorDto.setAgeId(null);
        arDonorDto.setDonorSampleKey(null);
        arDonorDto.setAgeList(null);
        arDonorDto.setAge(null);
        arDonorDto.setDonorSampleAgeDtos(null);
        arDonorDto.setResetDonor(null);
        arDonorDto.setRelation(null);
        arDonorDto.setDonorIdentityKnown(null);
        arDonorDto.setDonorSampleId(null);
    }

    protected void setEmptyDataForNullDrDonorDto(DonorDto arDonorDto){
        arDonorDto.setIdType(StringUtil.getStringEmptyToNull( arDonorDto.getIdType()));
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setIdNumber(StringUtil.getStringEmptyToNull( arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setSource(StringUtil.getStringEmptyToNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getStringEmptyToNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getStringEmptyToNull(arDonorDto.getDonorSampleCode()));
        }

        if(IaisCommonUtils.isNotEmpty(arDonorDto.getDonorSampleAgeDtos())){
            if( StringUtil.isNotEmpty(arDonorDto.getAge())){
                arDonorDto.getDonorSampleAgeDtos().stream().forEach(donorSampleAgeDto -> {
                            if(donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getAge())){
                                arDonorDto.setAgeId(arDonorDto.getAge());
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                            }else {
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                            }
                        }
                );
            }else {
                arDonorDto.setAgeId(null);
            }
        }

    }

    protected Map<Object, ValidationProperty> getByArCycleStageDto(List<DonorDto> donorDtos){
        Map<Object,ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(donorDtos)){
            donorDtos.forEach( arDonorDto -> {
                        validationPropertyList.put(arDonorDto,new ValidationProperty(arDonorDto.getArDonorIndex(),null,String.valueOf(arDonorDto.getArDonorIndex())));
                    }
            );
        }
        return validationPropertyList;
    }

    protected void setDonorDtos(HttpServletRequest request, List<DonorDto> arDonorDtos,boolean needPleaseIndicate){
        if(IaisCommonUtils.isNotEmpty(arDonorDtos)){
            arDonorDtos.forEach(arDonorDto -> {
                String arDonorIndex = String.valueOf(arDonorDto.getArDonorIndex());
                ControllerHelper.get(request,arDonorDto,arDonorIndex);
                if(needPleaseIndicate){
                    arDonorDto.setPleaseIndicate(ParamUtil.getStringsToString(request,"pleaseIndicate"+arDonorIndex));
                    arDonorDto.setPleaseIndicateValues(ParamUtil.getListStrings(request,"pleaseIndicate"+arDonorIndex));
                }
                clearNoClearDataForDrDonorDto(arDonorDto,request);
                if(needPleaseIndicate){
                    setArDonorDtoByPleaseIndicate(arDonorDto);
                }
            });
        }
    }

    private void setArDonorDtoByPleaseIndicate(DonorDto arDonorDto){
        if(StringUtil.isNotEmpty(arDonorDto.getPleaseIndicate())){
            String pleaseIndicate = arDonorDto.getPleaseIndicate();
            arDonorDto.setDonorIndicateEmbryo(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_EMBRYO_USED));
            arDonorDto.setDonorIndicateFresh(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FRESH_OOCYTE));
            arDonorDto.setDonorIndicateFrozen(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FROZEN_OOCYTE_USED));
            arDonorDto.setDonorIndicateSperm(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_SPERMS_USED));
        }
    }



    protected List<SelectOption> getSampleDropDown(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList(4);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_ID_TYPE_CODE,"Code"));
        MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE).stream().forEach(
                obj -> selectOptions.add(new SelectOption(obj.getCode(),obj.getCodeValue()))
        );
        return selectOptions;
    }


    protected String checkDonorsVerifyPass(List<DonorDto> arDonorDtos,HttpServletRequest request){
        if(ACTION_TYPE_CONFIRM.equalsIgnoreCase(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE)) && IaisCommonUtils.isNotEmpty(arDonorDtos)){
            Map<String, String> errorMap = (Map<String, String>) ParamUtil.getRequestAttr(request,IaisEGPConstant.ERRORMAP);
            if(IaisCommonUtils.isEmpty(errorMap)){
                return AppConsts.YES;
            }
            for(String key : errorMap.keySet()){
                if(!(key.contains("age")
                        || key.contains("relation")
                        || key.contains("source")
                         || key.contains("otherSource")
                        )){
                    return AppConsts.YES;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (DonorDto donorDto : arDonorDtos) {
                if (StringUtil.isEmpty(donorDto.getDonorSampleKey())) {
                    stringBuilder.append(" Donor ").append(donorDto.getArDonorIndex()+1).append(',');
                }
            }
            if(StringUtil.isEmpty(stringBuilder.toString())){
                return AppConsts.YES;
            }else {
                String message = stringBuilder.toString();
                 ParamUtil.setRequestAttr(request,"DS_ERR019Message",MessageUtil.replaceMessage("DS_ERR019",message.substring(0,message.length()-1),"field"));
                return AppConsts.NO;
            }
        }
        return AppConsts.YES;
    }

    //1 -> ar , 2 -> iui
    protected void initOldDonorSelectSession(HttpServletRequest request,int stage){
        if(isRfc(request)){
            List<DonorDto> arDonorDtos = null;
            ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
            if(stage == 1 && arSuperDataSubmissionDto != null && arSuperDataSubmissionDto.getArCycleStageDto()!= null){
                arDonorDtos = arSuperDataSubmissionDto.getArCycleStageDto().getOldDonorDtos();
            }else if(stage == 2 && arSuperDataSubmissionDto != null && arSuperDataSubmissionDto.getIuiCycleStageDto()!= null){
                arDonorDtos = arSuperDataSubmissionDto.getIuiCycleStageDto().getOldDonorDtos();
            }
            if(IaisCommonUtils.isNotEmpty(arDonorDtos)){
                List<DonorSampleAgeDto> donorSampleAgeDtos = IaisCommonUtils.genNewArrayList();
                for (DonorDto donorDto : arDonorDtos){
                    if(IaisCommonUtils.isNotEmpty(donorDto.getDonorSampleAgeDtos())){
                        for (DonorSampleAgeDto donorSampleAgeDto : donorDto.getDonorSampleAgeDtos()) {
                            if(donorSampleAgeDto.getId().equalsIgnoreCase(donorDto.getAge())){
                                donorSampleAgeDtos.add(donorSampleAgeDto);
                                break;
                            }
                        }
                    }
                }
                ParamUtil.setSessionAttr(request,OLD_DONORS_SELECTED,(Serializable) donorSampleAgeDtos);
            }
        }

    }
}
