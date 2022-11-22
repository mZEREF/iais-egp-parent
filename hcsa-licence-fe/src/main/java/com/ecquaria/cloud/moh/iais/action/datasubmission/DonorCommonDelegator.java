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
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
    private final static String  DONOR_USED_TYPES                  = "donorUsedTypes";
    private final static String ADD_DONOR_MAX_SIZE                 ="arAddDonorMaxSize";
    private final static String OLD_DONORS_SELECTED               ="oldDonorsSelected";
    private final static String DONOR_RESULT_SIZE                 = "donorResultSize";
    private final static String DONOR_MESSAGE_TIP                 = "donorMessageTip";
    protected void setDonorUserSession(HttpServletRequest request){
        String dspc004 = MasterCodeUtil.getCodeDesc("DSPC_004");
        ParamUtil.setSessionAttr(request, DONOR_USED_TYPES,(Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_DONOR_USED_TYPE));
        ParamUtil.setSessionAttr(request, DONOR_SOURSE_DROP_DOWN,(Serializable) getSourseList(request));
        ParamUtil.setSessionAttr(request, DONOR_SAMPLE_DROP_DOWN,(Serializable) getSampleDropDown());
        ParamUtil.setSessionAttr(request, ADD_DONOR_MAX_SIZE,SystemParamUtil.getSystemParamConfig().getArAddDonorMaxSize());
        ParamUtil.setSessionAttr(request, DONOR_RESULT_SIZE,dspc004);
        ParamUtil.setSessionAttr(request,DONOR_MESSAGE_TIP,MessageUtil.replaceMessage("DS_ERR053",dspc004,"1"));
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
        List<SelectOption> frozenOocyteAgeList = IaisCommonUtils.genNewArrayList();
        List<SelectOption> frozenEmbryoAgeList = IaisCommonUtils.genNewArrayList();
        List<SelectOption> frozenSpermAgeList = IaisCommonUtils.genNewArrayList();
        List<SelectOption> freshSpermAgeList = IaisCommonUtils.genNewArrayList();
        arDonorDto.getDonorSampleAgeDtos().stream().forEach(obj->{
            if(obj.getId().equalsIgnoreCase(arDonorDto.getAge())|| DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE.equalsIgnoreCase(obj.getStatus())){
                if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(obj.getSampleType())) {
                    ageList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
                } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(obj.getSampleType())) {
                    frozenOocyteAgeList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
                } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(obj.getSampleType())) {
                    frozenEmbryoAgeList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
                } else if(DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(obj.getSampleType())) {
                    frozenSpermAgeList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
                }else if(DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM.equals(obj.getSampleType())) {
                    freshSpermAgeList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
                }
            }
        });
        arDonorDto.setAgeList(ageList);
        arDonorDto.setFrozenOocyteAgeList(frozenOocyteAgeList);
        arDonorDto.setFrozenEmbryoAgeList(frozenEmbryoAgeList);
        arDonorDto.setFrozenSpermAgeList(frozenSpermAgeList);
        arDonorDto.setFreshSpermAgeList(freshSpermAgeList);
    }


    private DonorDto getInitArDonorDto(int arDonorIndex){
        DonorDto arDonorDto = new DonorDto();
        arDonorDto.setDirectedDonation(true);
        arDonorDto.setArDonorIndex(arDonorIndex);
        return arDonorDto;
    }

    protected void valiateDonorDtos(HttpServletRequest request,List<DonorDto> arDonorDtos){
        int valiateArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_VALIATE_DONOR);
        if(valiateArDonor >-1) {
            DonorDto arDonorDto = arDonorDtos.get(valiateArDonor);
            Map<String, String> errorMapVal = DonorValidator.valCommonField(IaisCommonUtils.genNewHashMap(2), arDonorDto, true);
            if (IaisCommonUtils.isNotEmpty(errorMapVal)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMapVal));
                return;
            }
            String idNumber = arDonorDto.getIdNumber();
            if (!arDonorDto.isDirectedDonation()){
                idNumber = arDonorDto.getDonorSampleCode();
            }
            ArSuperDataSubmissionDto arSuperDataSubmissionDto=DataSubmissionHelper.getCurrentArDataSubmission(request);
            String donorSampleKey = arDataSubmissionService.getDonorSampleKey(arDonorDto.getIdType(), idNumber);
            if (DataSubmissionConsts.DS_CYCLE_IUI.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
                donorSampleKey = arDataSubmissionService.getDonorSampleTypeKey(arDonorDto.getIdType(), idNumber, DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM).get(0);
            }
            List<DonorSampleAgeDto> allDonorSampleAgeDtos = arDataSubmissionService.getDonorSampleAgeDtoBySampleKey(donorSampleKey);
            List<DonorSampleAgeDto> donorSampleAgeDtos = IaisCommonUtils.genNewArrayList();
            for (DonorSampleAgeDto donorSampleAgeDto: allDonorSampleAgeDtos) {
                if(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE.equals(donorSampleAgeDto.getStatus())){
                    donorSampleAgeDtos.add(donorSampleAgeDto);
                }
            }
            //TODO, from ages
            int donorUseSize = 0;
            arDonorDto.setDonorIndicateFresh(false);
            arDonorDto.setDonorIndicateFrozen(false);
            arDonorDto.setDonorIndicateEmbryo(false);
            arDonorDto.setDonorIndicateFrozenSperm(false);
            arDonorDto.setDonorIndicateFreshSperm(false);
            if (donorSampleKey == null || IaisCommonUtils.isEmpty(donorSampleAgeDtos)) {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(2);
                String dsErr;
                if (donorSampleKey == null) {
                    errorMap.put("field1", "The corresponding donor");
                    errorMap.put("field2", "the AR centre");
                    dsErr = MessageUtil.getMessageDesc("DS_ERR012", errorMap).trim();
                } else {
                    errorMap.put("field", "The donor's age(s)");
                    dsErr = MessageUtil.getMessageDesc("DS_ERR020", errorMap).trim();
                }
                errorMap.clear();
                errorMap.put("validateDonor" +(arDonorDto.isDirectedDonation() ? "Yes" : "No") +arDonorDto.getArDonorIndex(),dsErr);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }else {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
                String finalDonorSampleKey = donorSampleKey;
                arDonorDtos.forEach( donorDto -> {
                    if (finalDonorSampleKey.equalsIgnoreCase(donorDto.getDonorSampleKey()) && donorDto.getArDonorIndex() != valiateArDonor) {
                        errorMap.put("validateDonor" + (arDonorDto.isDirectedDonation() ? "Yes" : "No") + arDonorDto.getArDonorIndex(), MessageUtil.replaceMessage("DS_ERR016", "This donor", "field"));
                        setDonorDtoByDonorSampleDto(donorDto, finalDonorSampleKey, donorSampleAgeDtos, donorUseSize, request);
                    }
                });
                if(errorMap.isEmpty()){
                    if (donorUseSize >= Integer.parseInt(MasterCodeUtil.getCodeDesc("DSPC_004"))) {
                        ParamUtil.setRequestAttr(request, "donorResultMoreValue", AppConsts.YES);
                    }
                    setDonorDtoByDonorSampleDto(arDonorDto, donorSampleKey, donorSampleAgeDtos, donorUseSize, request);
                } else {
                    clearDonorAges(arDonorDto);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                }
            }
        }
    }

    private void setDonorDtoByDonorSampleDto(DonorDto arDonorDto, String sampleKey, List<DonorSampleAgeDto> donorSampleAgeDtos, int useSize, HttpServletRequest request) {
        setRfcDonorSelectData(request, sampleKey, donorSampleAgeDtos);
        arDonorDto.setDonorSampleKey(sampleKey);
        arDonorDto.setDonorSampleAgeDtos(donorSampleAgeDtos);
        arDonorDto.setDonorUseSize(useSize);
        List<DonorSampleDto> donorSampleDtoLis = arDataSubmissionService.getDonorSampleDtoBySampleKey(sampleKey);
        //todo
        if (IaisCommonUtils.isNotEmpty(donorSampleDtoLis)){
            DonorSampleDto donorSampleDto = donorSampleDtoLis.get(0);
            arDonorDto.setDonorSampleId(donorSampleDto.getId());
            if(!donorSampleDto.getDirectedDonation()){
                arDonorDto.setSource(donorSampleDto.getSampleFromHciCode());
                arDonorDto.setOtherSource(donorSampleDto.getSampleFromOthers());
            } else {
                arDonorDto.setSource(donorSampleDto.getSampleFromHciCode());
            }
        }
        if (IaisCommonUtils.isNotEmpty(donorSampleAgeDtos)) {
            arDonorDto.setResetDonor(AppConsts.NO);
            setAgeList(arDonorDto);
        }
    }

    private void setRfcDonorSelectData(HttpServletRequest request, String sampleKey, List<DonorSampleAgeDto> donorSampleAgeDtos) {
        if (isRfc(request)) {
            List<DonorSampleAgeDto> oldDonorSampleAgeDtos = (List<DonorSampleAgeDto>) ParamUtil.getSessionAttr(request, OLD_DONORS_SELECTED);
            if (IaisCommonUtils.isNotEmpty(oldDonorSampleAgeDtos)) {
                List<DonorSampleAgeDto> ages = donorSampleAgeDtos;
                for (DonorSampleAgeDto donorSampleAgeDto : oldDonorSampleAgeDtos) {
                    if (sampleKey.equalsIgnoreCase(donorSampleAgeDto.getSampleKey())) {
                        boolean isHaveSel = false;
                        for (DonorSampleAgeDto donorSampleAgeDtoF : ages) {
                            if (donorSampleAgeDtoF.getId().equalsIgnoreCase(donorSampleAgeDto.getId())) {
                                isHaveSel = true;
                                break;
                            }
                        }
                        if (!isHaveSel) {
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
        arDonorDto.setDonorUseSize(0);
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
            if( StringUtil.isNotEmpty(arDonorDto.getAge()) || StringUtil.isNotEmpty(arDonorDto.getFrozenOocyteAge())
                    || StringUtil.isNotEmpty(arDonorDto.getFrozenEmbryoAge()) || StringUtil.isNotEmpty(arDonorDto.getFrozenSpermAge())
                    || StringUtil.isNotEmpty(arDonorDto.getFreshSpermAge())){
                arDonorDto.getDonorSampleAgeDtos().stream().forEach(donorSampleAgeDto -> {
                            if(donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getAge())){
                                arDonorDto.setAgeId(arDonorDto.getAge());
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                            }else if (donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getFrozenOocyteAge())){
                                arDonorDto.setFrozenOocyteAgeId(arDonorDto.getFrozenOocyteAge());
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                            }else if (donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getFrozenEmbryoAge())){
                                arDonorDto.setEmbryoAgeId(arDonorDto.getFrozenEmbryoAge());
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                            }else if (donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getFrozenSpermAge())){
                                arDonorDto.setFrozenSpermAgeId(arDonorDto.getFrozenSpermAge());
                                donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                            }else if (donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getFreshSpermAge())){
                                arDonorDto.setFreshSpermAgeId(arDonorDto.getFreshSpermAge());
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
                if (!needPleaseIndicate) {
                    arDonorDto.setFrozenSpermAge(ParamUtil.getString(request, "spermAge"+arDonorIndex));
                    arDonorDto.setAge(ParamUtil.getString(request, "spermAge"+arDonorIndex));
                }
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
            arDonorDto.setDonorIndicateFrozenSperm(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FROZEN_SPERMS_USED));
            arDonorDto.setDonorIndicateFreshSperm(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FRESH_SPERMS_USED));
        }
    }



    protected List<SelectOption> getSampleDropDown() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList(4);
        selectOptions.add(new SelectOption(DataSubmissionConsts.DTV_ID_TYPE_CODE, "Code"));
        MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE_DTV).forEach(
                obj -> selectOptions.add(new SelectOption(obj.getCode(), obj.getCodeValue()))
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
